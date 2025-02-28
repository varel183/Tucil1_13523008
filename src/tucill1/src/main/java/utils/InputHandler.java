package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import logic.Block;

public class InputHandler {
    private int N, M, P;
    private List<Block> blocks;
    private String caseType;

    public InputHandler(String filePath) throws FileNotFoundException {
        blocks = new ArrayList<>();
        readFile("test/input/" + filePath);
    }

    private void readFile(String filePath) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            // cek N, M, P, dan case type
            if (!scanner.hasNextInt()) {
                scanner.close();
                throw new IllegalArgumentException("Error: Nilai N tidak ditemukan dalam file atau bukan bilangan bulat.");
            }
            N = scanner.nextInt();

            if (!scanner.hasNextInt()) {
                scanner.close();
                throw new IllegalArgumentException("Error: Nilai M tidak ditemukan dalam file atau bukan bilangan bulat.");
            }
            M = scanner.nextInt();

            // cek N M positif
            if (N < 1 || M < 1) {
                scanner.close();
                throw new IllegalArgumentException("Error: Nilai N atau M tidak valid (positif).");
            }

            if (!scanner.hasNextInt()) {
                scanner.close();
                throw new IllegalArgumentException("Error: Nilai P tidak ditemukan dalam file atau bukan bilangan bulat.");
            }
            P = scanner.nextInt();

            // cek P harus lebih kecil dari 27
            if (P > 26) {
                scanner.close();
                throw new IllegalArgumentException("Error: Jumlah blok (" + P + ") melebihi batas maksimum (26).");
            }
            
            scanner.nextLine();
            
            caseType = scanner.nextLine();

            // cek caseType
            if (!caseType.equals("DEFAULT")) {
                scanner.close();
                throw new IllegalArgumentException("Error: Case type yang valid hanya 'DEFAULT'");
            }

            int blockCount = 0;
            List<List<Character>> currentBlock = new ArrayList<>();
            List<List<List<Character>>> blockShape = new ArrayList<>();
            char currentSymbol = '\0';
            List<Character> usedChar = new ArrayList<>();

            // proses pembacaan blok
            while (scanner.hasNext()) {
                String line = scanner.nextLine();

                if (line.isEmpty()) {
                    if (!currentBlock.isEmpty()) {
                        blockShape.add(new ArrayList<>(currentBlock));
                        currentBlock.clear();
                        blockCount++;
                    }
                    continue;
                }

                for (char c : line.toCharArray()) {
                    if (!isValidChar(c)) {
                        throw new IllegalArgumentException("Error: Karakter yang ditemukan (" + c + ") bukan huruf kapital atau spasi.");
                    }
                }

                char firstLetter = '.';
                for (char c : line.toCharArray()) {
                    if (Character.isUpperCase(c)) {
                        firstLetter = c;
                        break;
                    }
                }

                List<Character> processedRow = processLine(line);

                if (firstLetter != '.') {
                    if (currentSymbol == '\0') {
                        currentSymbol = firstLetter;
                        currentBlock.add(processedRow);
                    } else if (currentSymbol == firstLetter) {
                        currentBlock.add(processedRow);
                    } else {
                        if (!currentBlock.isEmpty()) {
                            blockShape.add(new ArrayList<>(currentBlock));
                            currentBlock.clear();
                            blockCount++;
                        }
                        currentSymbol = firstLetter;
                        currentBlock.add(processedRow);
                    }
                }
            }

            if (!currentBlock.isEmpty()) {
                blockShape.add(new ArrayList<>(currentBlock));
                blockCount++;
            }

            if (blockCount < P) {
                scanner.close();
                throw new IllegalArgumentException("Error: Jumlah blok yang terbaca (" + blockCount + ") kurang dari jumlah yang diharapkan (" + P + ").");
            } 

            // buat jadi square
            for (List<List<Character>> block : blockShape) {
                List<List<Character>> squareBlock = makeSquare(block);
                Block newBlock = new Block(squareBlock);

                char symbol = newBlock.getSymbol();
                if (usedChar.contains(symbol)) {
                    throw new IllegalArgumentException("Error: Simbol blok (" + symbol + ") sudah digunakan.");
                }
                usedChar.add(symbol);
                blocks.add(newBlock);
            }
            
            scanner.close();
        }
    }

    private boolean isValidChar(char c) {
        return (Character.isUpperCase(c)) || c == ' ';
    }

    private List<Character> processLine(String line) {
        List<Character> processedRow = new ArrayList<>();
        boolean foundFirstLetter = false;
        
        for (char c : line.toCharArray()) {
            if (Character.isUpperCase(c)) {
                foundFirstLetter = true;
                processedRow.add(c);
            } else if (!foundFirstLetter && c == ' ') {
                processedRow.add('.');
            } else {
                processedRow.add('.');
            }
        }
        
        return processedRow;
    }

    

    private static List<List<Character>> makeSquare(List<List<Character>> block) {
        int rows = block.size();
        int cols = 0;
        
        for (List<Character> row : block) {
            cols = Math.max(cols, row.size());
        }

        int max = Math.max(rows, cols);

        List<List<Character>> squareBlock = new ArrayList<>();
        for (List<Character> row : block) {
            List<Character> newRow = new ArrayList<>(row);
            while (newRow.size() < max) {
                newRow.add('.');
            }
            squareBlock.add(newRow);
        }

        while (squareBlock.size() < max) {
            List<Character> row = new ArrayList<>();
            while (row.size() < max) {
                row.add('.');
            }
            squareBlock.add(row);
        }

        return squareBlock;
    }

    public int getN() {return N;}
    public int getM() {return M;}
    public int getP() {return P;}
    public String getCaseType() {return caseType;}
    public List<Block> getBlocks() {return blocks;}
}