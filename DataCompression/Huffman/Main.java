package SiAOD.DataCompression.Huffman;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;
public class Main
{
    public static void fileWrite(String name, String data)
    {
        try (FileOutputStream file = new FileOutputStream(name))
        {
            System.out.print("\n\nЗаписано в файл: " + data);
            file.write(data.getBytes());
        }
        catch (IOException exc)
        {
            System.out.println(exc);
            System.exit(1);
        }
    }
    public static String fileRead(String name)
    {
        try (FileInputStream file = new FileInputStream(name))
        {
            int it;
            StringBuilder str = new StringBuilder();
            while ((it = file.read()) != '\n')
                str.append(it);
            return str.toString();
        }
        catch (IOException exc)
        {
            System.out.println(exc);
            System.exit(1);
        }
        return "";
    }
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите строку для сжатия: ");
        String text = scanner.nextLine();
        System.out.print("Введите название файла для записи строки: ");
        String fileName = scanner.nextLine();
        System.out.println("Исходная строка: " + text);
        HuffmanCompression huffmanCompression = new HuffmanCompression(text);
        StringBuilder code = new StringBuilder();
        int cnt = 0;
        System.out.println("Коды всех символов в строке:");
        for (char ch : huffmanCompression.huffmanCode().keySet())
            System.out.println("Для символа '" + ch + "' частота равна: " + huffmanCompression.getFrequency().get(ch) +
                    ", код: " + huffmanCompression.getPath().get(ch));
        System.out.print("\nПоследовательность кодов исходной строки: ");
        for (char ch : text.toCharArray())
        {
            if (++cnt > 8)
            {
                System.out.println();
                cnt = 0;
            }
            System.out.print(Integer.toBinaryString(ch) + ".");
        }
        fileWrite(fileName, huffmanCompression.codedText(text).replace(".", ""));
        System.out.println("\n\nПоследовательность кодов сжатой строки: " + huffmanCompression.codedText(text));
        for (char ch : huffmanCompression.getPath().keySet())
        {
            for (int i = 0; i < huffmanCompression.getPath().get(ch).size(); i++)
                code.append(huffmanCompression.getPath().get(ch).get(i));
            code.append(".");
        }
        System.out.println("\nКоды каждого уникального символа в строке: " + code);
        System.out.println("Длина исходной строки: " + text.length() * 8);
        System.out.println("Длина закодированной строки: " + huffmanCompression.countCode());
        System.out.println("Декодированная строка: " +
                huffmanCompression.decode(huffmanCompression.codedText(text).replace(".", "")));
        scanner.close();
    }
}