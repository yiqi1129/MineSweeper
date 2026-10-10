import java.util.Random;

public class Grid {
    protected int width;
    protected int height;
    protected int mines;
    protected int bombsLeft;
    protected char[][] player_grid;
    protected int[][] bomb_grid;
    protected boolean bombsPlaced = false;

    public Grid(int width, int height, int mines) {
        this.width = width;
        this.height = height;
        this.mines = mines;
        this.bombsLeft = mines;
        player_grid = new char[height][width];
        bomb_grid = new int[height][width];
        initializeGrids();
    }

    protected void initializeGrids() {
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                player_grid[r][c] = '#';
                bomb_grid[r][c] = 0;
            }
        }
    }

    protected void calculateNumbers() {
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                if (bomb_grid[r][c] == -1) continue;
                int count = 0;
                for (int dr = -1; dr <= 1; dr++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        int nr = r + dr;
                        int nc = c + dc;

                        if (nr >= 0 && nr < height && nc >= 0 && nc < width) {
                            if (bomb_grid[nr][nc] == -1) count++;
                        }
                    }
                }
                bomb_grid[r][c] = count;
            }
        }
    }

    protected void placeBombs(int firstR, int firstC) {
        Random rand = new Random();
        int placed = 0;
        while (placed < mines) {
            int r = rand.nextInt(height);
            int c = rand.nextInt(width);
            if (bomb_grid[r][c] == -1 || (r == firstR && c == firstC)) continue;
            bomb_grid[r][c] = -1;
            placed++;
        }
        calculateNumbers();
    }

    public boolean click(int r, int c) {
        if (outOfBounds(r, c)) return true;
        if (player_grid[r][c] == 'F') return true;
        if (player_grid[r][c] != '#') return true;
        if (!bombsPlaced) {
            placeBombs(r, c);
            bombsPlaced = true;
        }

        if (bomb_grid[r][c] == -1) {
            player_grid[r][c] = '*';
            return false;
        }
        reveal(r, c);
        return true;
    }
    //r is x, c is y, dr is the change of x, dc is the change of y.
    private void reveal(int r, int c) {
        if (outOfBounds(r, c)) return;
        if (player_grid[r][c] != '#') return;
        if (bomb_grid[r][c] == 0) {
            player_grid[r][c] = '0';
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    reveal(r + dr, c + dc);
                }
            }
        } else {
            player_grid[r][c] = (char)(bomb_grid[r][c] + '0');
        }
    }

    public void toggleFlag(int r, int c) {
        if (outOfBounds(r, c)) return;
        if (player_grid[r][c] == '#') {
            player_grid[r][c] = 'F';
            bombsLeft--;
        } else if (player_grid[r][c] == 'F') {
            player_grid[r][c] = '#';
            bombsLeft++;
        }
    }

    public boolean checkWin() {
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                if (player_grid[r][c] == '#' && bomb_grid[r][c] != -1) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean outOfBounds(int r, int c) {
        return r < 0 || r >= height || c < 0 || c >= width;
    }

    public String toString() {
        String out = "";
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                out += player_grid[r][c] + " ";
            }
            out += "\n";
        }
        return out;
    }

    public void revealAll() {
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                if (bomb_grid[r][c] == -1) {
                    player_grid[r][c] = '*';
                } else if (bomb_grid[r][c] == 0) {
                    player_grid[r][c] = '0';
                } else {
                    player_grid[r][c] = (char)(bomb_grid[r][c] + '0');
                }
            }
        }
    }
}

class Beginner extends Grid {
    public Beginner() { super(9, 9, 10); }
}

class Intermediate extends Grid {
    public Intermediate() { super(16, 16, 40); }
}

class Expert extends Grid {
    public Expert() { super(30, 16, 99); }
}