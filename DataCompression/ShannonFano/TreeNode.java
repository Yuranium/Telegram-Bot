package SiAOD.DataCompression.ShannonFano;

public class TreeNode // Класс, представляющий собой узел в дереве Шеннона-Фано
{
    private final float probabilities; // Вероятность появления символа с исходной строки
    private final char symbol; // Символ из строки
    TreeNode left; // Сслыка на левое поддерево
    TreeNode right; // Ссылка на правое поддерево
    TreeNode() // Конструктор, в случае, если нужно создать промежуточный узел
    {
        this.probabilities = 0F;
        this.symbol = '\u0000';
    }
    TreeNode(float probabilities, char symbol) // А это если, нужен узел с исходными данными
    {
        this.probabilities = probabilities;
        this.symbol = symbol;
    }
    public float getProbabilities()
    {
        return probabilities;
    }
    public char getSymbol()
    {
        return symbol;
    }
}