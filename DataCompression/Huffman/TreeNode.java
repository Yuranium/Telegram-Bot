package SiAOD.DataCompression.Huffman;
public class TreeNode implements Comparable<TreeNode> // Класс, представляющий узел в дереве Хаффмана
{
    int weight; // Количество появления символа в исходной строке
    char symbol; // Сам символ
    TreeNode left; // Ссылка на левое поддерево
    TreeNode right; // Ссылка на правое поддерево
    TreeNode(int weight)
    {
        this.weight = weight;
    }
    TreeNode(int weight, char symbol)
    {
        this.weight = weight;
        this.symbol = symbol;
    }
    // Метод, позволяющий сортировать объекты по весам
    @Override
    public int compareTo(TreeNode object)
    {
        return object.weight - this.weight;
    }
}