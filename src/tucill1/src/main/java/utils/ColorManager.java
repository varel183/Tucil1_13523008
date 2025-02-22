package utils;

import javafx.scene.paint.Color;

public class ColorManager {
    public static final Color EMPTY_CELL = Color.WHITE;
    public static final Color EMPTY_CELL_BORDER = Color.LIGHTGRAY;
    public static final Color BLOCK_BORDER = Color.BLACK;
    public static final Color TRANSPARENT = Color.TRANSPARENT;
    public static final Color DEFAULT_BLOCK = Color.BLACK;
    
    public static Color getBlockColor(char symbol) {
        switch (symbol) {
            case 'A': return Color.RED;
            case 'B': return Color.BLUE;
            case 'C': return Color.GREEN;
            case 'D': return Color.PURPLE;
            case 'E': return Color.ORANGE;
            case 'F': return Color.CYAN;
            case 'G': return Color.MAGENTA;
            case 'H': return Color.BROWN;
            case 'I': return Color.DARKBLUE;
            case 'J': return Color.DARKGREEN;
            case 'K': return Color.DARKORANGE;
            case 'L': return Color.DARKRED;
            case 'M': return Color.LIGHTBLUE;
            case 'N': return Color.LIGHTGREEN;
            case 'O': return Color.LIGHTPINK;
            case 'P': return Color.LIGHTYELLOW;
            case 'Q': return Color.DARKGRAY;
            case 'R': return Color.GOLD;
            case 'S': return Color.SILVER;
            case 'T': return Color.TEAL;
            case 'U': return Color.VIOLET;
            case 'V': return Color.TOMATO;
            case 'W': return Color.SALMON;
            case 'X': return Color.OLIVE;
            case 'Y': return Color.INDIANRED;
            case 'Z': return Color.CORNFLOWERBLUE;
            default: return DEFAULT_BLOCK;
        }
    }

    public static String getHexColor(char symbol) {
        switch (symbol) {
            case 'A': return "#FF0000"; // Red
            case 'B': return "#0000FF"; // Blue
            case 'C': return "#00FF00"; // Green
            case 'D': return "#800080"; // Purple
            case 'E': return "#FFA500"; // Orange
            case 'F': return "#00FFFF"; // Cyan
            case 'G': return "#FF00FF"; // Magenta
            case 'H': return "#A52A2A"; // Brown
            case 'I': return "#00008B"; // Dark Blue
            case 'J': return "#006400"; // Dark Green
            case 'K': return "#FF8C00"; // Dark Orange
            case 'L': return "#8B0000"; // Dark Red
            case 'M': return "#ADD8E6"; // Light Blue
            case 'N': return "#90EE90"; // Light Green
            case 'O': return "#FFB6C1"; // Light Pink
            case 'P': return "#FFFFE0"; // Light Yellow
            case 'Q': return "#A9A9A9"; // Dark Gray
            case 'R': return "#FFD700"; // Gold
            case 'S': return "#C0C0C0"; // Silver
            case 'T': return "#008080"; // Teal
            case 'U': return "#EE82EE"; // Violet
            case 'V': return "#FF6347"; // Tomato
            case 'W': return "#FA8072"; // Salmon
            case 'X': return "#808000"; // Olive
            case 'Y': return "#CD5C5C"; // Indian Red
            case 'Z': return "#6495ED"; // Cornflower Blue
            default: return "#000000"; // Black (default)
        }
    }
}