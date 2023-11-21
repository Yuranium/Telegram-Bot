package SiAOD.DataCompression.ShannonFano;
import java.util.ArrayList;
import java.util.HashMap;

public class ShannonFanoCompression // Класс, реализующий сжатие по методу Шеннона-Фано
{
    private final HashMap<Character, Float> probabilities = new HashMap<>(); // Хеш-таблица, хранящая вероятности
    private final HashMap<Character, ArrayList<Integer>> path = new HashMap<>(); // Хеш-таблица, хранящая путь от корня до листа с символом
    private TreeNode root = new TreeNode(); // Ссылка на корень дерева Шеннона-Фано
    ShannonFanoCompression(String text) // Конструктор
    {
        Integer count; // Количество повторений символов
        int size = text.length(); // Для вычисления вероятности символа из строки
        HashMap<Character, Integer> frequency = new HashMap<>(); // Хеш-таблица для хранения частотности символов
        for (char ch : text.toCharArray()) // По всей строке
        {
             count = frequency.get(ch);
             frequency.put(ch, (count == null) ? 1 : ++count); // Проверка на уникальность, или инкрементирование частотности
             probabilities.put(ch, (float) frequency.get(ch) / size); // Добавление в хеш-таблицу частотности и символа
        }
    }
    public void generateTree() // Метод-обёртка для построения дерева Шеннона-Фано
    {
        ArrayList<TreeNode> nodes = new ArrayList<>(); // Список узлов дерева
        for (char ch : probabilities.keySet()) // По таблице
            nodes.add(new TreeNode(probabilities.get(ch), ch)); // Создаём пока что только листы дерева
        // Сортировка листов дерева по убыванию их вероятностей
        nodes.sort((s1, s2) -> Float.compare(s2.getProbabilities(), s1.getProbabilities()));
        root = buildShannonFanoTree(nodes, root); // Присвоение поддерева корню
    }
    private TreeNode buildShannonFanoTree(ArrayList<TreeNode> subarray, TreeNode parent) // Метод для построения дерева
    {
        if (subarray.size() == 1) // Если 1 элемент, дальше можно не строить дерево
            return subarray.get(0);
        else
        {
            float totalProbabilities = 0F; // Итоговая вероятность
            for (TreeNode node : subarray) // По всему списку листов
                totalProbabilities += node.getProbabilities(); // Вычисление итоговой вероятности
            float halfProbabilities = totalProbabilities / 2; // Половина от вероятности
            float separation = 0F; // Переменная для вычисления индекса разделения
            int index = 0; // Переменная, благодаря которой будет рекурсивное деление списка
            while (separation < halfProbabilities) // Пока не будут уравновешены вероятности слева и справа
                separation += subarray.get(index++).getProbabilities(); // Добавляем "вес" слева
            TreeNode leftNode = new TreeNode(); // Создаем новый корень для левого поддерева
            TreeNode rightNode = new TreeNode(); // Создаем новый корень для правого поддерева
            if (subarray.size() == 3) // При возникновении неоднозначности распределения узлов
                index = 1;
            // Рекурсивно строим левую и правую части дерева для текущего узла
            parent.left = buildShannonFanoTree(new ArrayList<>(subarray.subList(0, index)), leftNode);
            parent.right = buildShannonFanoTree(new ArrayList<>(subarray.subList(index, subarray.size())), rightNode);
            return parent; // Возвращаем текущий узел
        }
    }
    public HashMap<Character, ArrayList<Integer>> getCode() // Метод-обёртка для генерирования кодов Шеннона-Фано
    {
        generateShF_Code(root, new ArrayList<>()); // Вызываем метод для генерации кодов
        return path; // Возврат хеш-таблицы путей символов от корня дерева
    }
    private void generateShF_Code(TreeNode node, ArrayList<Integer> currentCode) // Вспомогательный метод
    {
        if (node == null) // Ошибка
            return;
        if (node.left == null && node.right == null) // Если это лист
            path.put(node.getSymbol(), new ArrayList<>(currentCode)); // Лист - это конечный путь кодирования, добавляем путь
        else
        {
            currentCode.add(0); // Добавляем к пути 0, т.е идём влево по дереву
            generateShF_Code(node.left, currentCode); // Рекурсивно идём влево
            currentCode.remove(currentCode.size() - 1); // Удаляем символ, т.к можем попасть в null
            currentCode.add(1); // Аналогично с правым поддеревом
            generateShF_Code(node.right, currentCode);
            currentCode.remove(currentCode.size() - 1);
        }
    }
    public String stringCode(String text) // Метод для составления закодированной строки
    {
        if (path.isEmpty()) // Если таблица путей пуста => нет кодов => невозможно построить закодированную строку
            throw new IllegalArgumentException("The symbol code table is empty");
        StringBuilder code = new StringBuilder(); // Строка, состоящая из кодов
        for (char ch : text.toCharArray()) // По всей исходной строке
        {
            for (int i : path.get(ch)) // Получаем список путей из 1 и 0
                code.append(i); // Каждую 1 или 0 добавляем в строку, чтобы получить код
            code.append("."); // Для удобства восприятия
        }
        return new String(code); // Возврат итоговой кодовой строки
    }
    public String decodeText(String codetext) // Метод для декодирования закодированной строки
    {
        StringBuilder text = new StringBuilder(); // Декодированная строка
        TreeNode node = root; // Для движения по дереву и раскодировки
        for (int i = 0; i < codetext.length(); i++) // По всей закодированной строке
        {
            node = (codetext.charAt(i) == '1') ? node.right : node.left; // В зависимости от 0 или 1 идём влево или вправо
            if (node.left == null && node.right == null) // Если дошли до листа, то мы раскодировали один символ
            {
                text.append(node.getSymbol()); // Добавляем этот символ в декодированную строку
                node = root; // Возвращаемся к корню дерева для декодирования следующего символа
            }
        }
        return text.toString(); // Возврат декодированной строки
    }
    public void codeShow() // Метод для вывода в консоль всех уникальных символов стоки и их вероятностей
    {
        System.out.println("\nВероятность появления каждого символа из строки:");
        for (char ch : probabilities.keySet()) // По всей хеш-таблице вероятностей
            System.out.println("Вероятность появления символа '" + ch + "' в строке равна: " + probabilities.get(ch));
    }
}