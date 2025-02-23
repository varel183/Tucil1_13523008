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

            if (!scanner.hasNextInt()) {
                scanner.close();
                throw new IllegalArgumentException("Error: Nilai P tidak ditemukan dalam file atau bukan bilangan bulat.");
            }
            P = scanner.nextInt();

            if (P > 26) {
                scanner.close();
                throw new IllegalArgumentException("Error: Jumlah blok (" + P + ") melebihi batas maksimum (26).");
            }

            scanner.nextLine();

            if (!scanner.hasNextLine()) {
                scanner.close();
                throw new IllegalArgumentException("Error: Case type tidak ditemukan dalam file.");
            }
            caseType = scanner.nextLine();

            int blockCount = 0;
            List<List<Character>> currentBlock = new ArrayList<>();
            List<List<List<Character>>> blockShape = new ArrayList<>();
            char currentSymbol = '\0';

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
                        throw new IllegalArgumentException("Error: Karakter yang ditemukan (" + c + ") bukan huruf atau spasi.");
                    }
                }

                char firstLetter = '.';
                for (char c : line.toCharArray()) {
                    if (Character.isLetter(c)) {
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

            for (List<List<Character>> block : blockShape) {
                List<List<Character>> squareBlock = makeSquare(block);
                Block newBlock = new Block(squareBlock);
                blocks.add(newBlock);
            }
            
            scanner.close();
        }
    }

    private boolean isValidChar(char c) {
        return Character.isLetter(c) || c == ' ';
    }

    private List<Character> processLine(String line) {
        List<Character> processedRow = new ArrayList<>();
        boolean foundFirstLetter = false;
        
        for (char c : line.toCharArray()) {
            if (Character.isLetter(c)) {
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

    public int getN() {
        return N;
    }

    public int getM() {
        return M;
    }

    public int getP() {
        return P;
    }

    public String getCaseType() {
        return caseType;
    }

    public List<Block> getBlocks() {
        return blocks;
    }
}