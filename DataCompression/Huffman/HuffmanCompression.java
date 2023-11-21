package SiAOD.DataCompression.Huffman;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;

public class HuffmanCompression
{
    // Хеш-таблица, представляющая собой частоту появления символов в строке
    private final HashMap<Character, Integer> frequency = new HashMap<>();
    // Хеш-таблица, представляющая собой путь до каждого символа в дереве Хаффмана
    private final HashMap<Character, ArrayList<Integer>> path = new HashMap<>();
    private final TreeNode root; // Ссылка на корень дерева Хаффмана
    HuffmanCompression(String str) // Конструктор, который сразу составляет таблицу частотности символов
    {
        for (int i = 0; i < str.length(); i++)
        {
            Character ch = str.charAt(i); // Считываем каждый отдельный символ
            Integer count = frequency.get(ch); // Достаём по этому ключу количество появления символа в строке
            frequency.put(ch, (count == null) ? 1 : ++count); // В зависимости от отсутствия или присутствия
            // добавляем или инкрементируем счётчик появления символа в строке
        }
        root = buildTree(); // Во время создания объекта строим дерево, начиная с корня
    }
    public HashMap<Character, ArrayList<Integer>> getPath()
    {
        return path;
    }
    public HashMap<Character, Integer> getFrequency()
    {
        return frequency;
    }
    public String codedText(String code) // Метод, кодирующий исходную строку
    {
        StringBuilder codedText = new StringBuilder(); // Строка с кодами символов
        for (char ch : code.toCharArray()) // Идём по строке
        {
            for (int i : path.get(ch)) // Идём по пути из 0 и 1 и добавляем их в строку
                codedText.append(i);
            codedText.append("."); // Для удобства чтения добавляем после каждого закодированного символа точку
        }
        return new String(codedText);
    }
    private TreeNode buildTree() // Метод построения дерева Хаффмана
    {
        ArrayList<TreeNode> nodes = new ArrayList<>(); // Список из узлов
        for (Character ch : frequency.keySet()) // Перебираем каждый уникальный символ из строки
            nodes.add(new TreeNode(frequency.get(ch), ch)); // Создаём лист дерева с суммой повторений и символом и добавляем в список
        TreeNode node = null; // Для построения связей
        while (nodes.size() > 1) // Пока не останется 1 элемент в списке
        {
            Collections.sort(nodes); // Сортируем в порядке убывания объекты по частотности
            node = new TreeNode(nodes.get(nodes.size() - 1).weight + nodes.get(nodes.size() - 2).weight); // Создаём родительский узел
            // дерева от двух минимальных в списке
            node.left = nodes.remove(nodes.size() - 1); // Добавляем связь к удалённым узлам из списка
            node.right =  nodes.remove(nodes.size() - 1);
            nodes.add(node); // Кладём новый родительский узел в список
        }
        return node; // Возвращаем корень дерева
    }
    public HashMap<Character, ArrayList<Integer>> huffmanCode() // Метод-обёртка для составления кодов Хаффмана
    {
        generateHuffmanCode(root, new ArrayList<>());
        return path; // Возвращаем изменённую хеш-таблицу путей
    }
    private void generateHuffmanCode(TreeNode node, ArrayList<Integer> currentCode) // Вспомогательный метод
    {
        if (node == null)
            return;
        if (node.left == null && node.right == null) // Если это лист
            path.put(node.symbol, new ArrayList<>(currentCode)); // Лист - это конечный путь кодирования, добавляем путь
        else
        {
            currentCode.add(0); // Добавляем к пути 0, т.е идём влево по дереву
            generateHuffmanCode(node.left, currentCode); // Рекурсивно идём влево
            currentCode.remove(currentCode.size() - 1); // Удаляем символ, т.к можем попасть в null
            currentCode.add(1); // Аналогично с правым поддеревом
            generateHuffmanCode(node.right, currentCode);
            currentCode.remove(currentCode.size() - 1);
        }
    }
    public String decode(String code) // Метод, декодирующий код Хаффмана
    {
        if (code.isEmpty()) // Если пусто, то идти дальше нет смысла
            return "";
        TreeNode node = root; // Корень
        StringBuilder decodedText = new StringBuilder(); // Строка с декодированным текстом
        for (int i = 0; i < code.length(); i++) // По всей строке, состоящей из кодов
        {
            node = code.charAt(i) == '0' ? node.left : node.right; // В зависимости от 0 или 1 двигаемся влево или вправо по дереву
            if (node.left == null && node.right == null) // Если дошли до листа дерева => нашёлся символ
            {
                decodedText.append(node.symbol); // Добавляем этот символ в итоговую строку
                node = root; // Возвращаемся обратно к корню дерева
            }
        }
        return new String(decodedText); // Возвращаем разкодированную строку
    }
    public int countCode() // Метод, подсчитывающий длину закодированной строки
    {
        if (path.isEmpty()) // Если пусто, возврат 0
            return 0;
        int count = 0; // Счётчик
        for (char ch : path.keySet()) // Двигаемся массиву у каждого уникального символа
            count += path.get(ch).size() * frequency.get(ch); // Вычисление длины закодированной строки, путём
        // умножения длины массива у символа с частотой его появления в исходной строке
        return count;
    }
}