package SiAOD.DataCompression.ShannonFano;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main
{
    public static void fileWrite(String name, String data)
    {
        try (FileWriter file = new FileWriter(name))
        {
            file.write(data);
        }
        catch (IOException exc)
        {
            System.out.println(exc);
            System.exit(1);
        }
    }
    public static String fileRead(String name)
    {
        try (FileReader file = new FileReader(name))
        {
            Scanner sc = new Scanner(file);
            return sc.nextLine();
        }
        catch (IOException exc)
        {
            System.out.println(exc);
            System.exit(1);
        }
        return "Error";
    }
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        System.out.print("Введите строку для сжатия: ");
        String text = sc.nextLine();
        System.out.print("Введите имя файла для записи строки: ");
        String file = sc.nextLine();
        ShannonFanoCompression sh = new ShannonFanoCompression(text);
        sh.codeShow();
        sh.generateTree(); // Генерирование дерева Шеннона-Фано для дальнейшей работы
        System.out.println("Коды для каждого символа из строки:");
        for (char ch : sh.getCode().keySet())
            System.out.println("Для символа '" + ch + "' код: " + sh.getCode().get(ch));
        System.out.println("\nИсходная строка: " + text);
        System.out.println("Исходная строка в двоичном представлении:");
        for (int i = 0; i < text.length(); i++)
        {
            if (i % 6 == 0 && i != 0)
                System.out.println();
            System.out.print(Integer.toBinaryString(text.charAt(i)) + ".");
        }
        String code = sh.stringCode(text).replace(".", "");
        fileWrite(file, code);
        System.out.println("\n\nДлина исходной строки в битах: " + text.length() * 8);
        System.out.println("Длина закодированной строки в битах: " + code.length());
        System.out.println("\nЗакодированная строка: " + sh.stringCode(text));
        System.out.println("Декодированная обратно стока: " + sh.decodeText(fileRead(file)));
        sc.close();
    }
}