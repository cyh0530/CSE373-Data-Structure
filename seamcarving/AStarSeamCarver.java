package seamcarving;

import astar.AStarGraph;
import astar.AStarSolver;
import astar.ShortestPathsSolver;
import astar.WeightedEdge;
import edu.princeton.cs.algs4.Picture;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class AStarSeamCarver implements SeamCarver {
    private Picture picture;

    public AStarSeamCarver(Picture picture) {
        if (picture == null) {
            throw new NullPointerException("Picture cannot be null.");
        }
        this.picture = new Picture(picture);
    }

    public Picture picture() {
        return new Picture(picture);
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public int width() {
        return picture.width();
    }

    public int height() {
        return picture.height();
    }

    public Color get(int x, int y) {
        return picture.get(x, y);
    }

    public double energy(int x, int y) {
        int width = width();
        int height = height();
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IndexOutOfBoundsException();
        }

        int left  = x - 1;
        int right = x + 1;
        int top   = y - 1;
        int bot   = y + 1;
        if (left == -1) {
            left = width - 1;
        } else if (right >= width) {
            right = 0;
        }

        if (top == -1) {
            top = height - 1;
        } else if (bot >= height) {
            bot = 0;
        }

        Color pixelLeft = picture.get(left, y);
        Color pixelRight = picture.get(right, y);
        Color pixelTop = picture.get(x, top);
        Color pixelBot = picture.get(x, bot);

        double deltaXSquare = Math.pow(pixelLeft.getRed() - pixelRight.getRed(), 2) +
                              Math.pow(pixelLeft.getGreen() - pixelRight.getGreen(), 2) +
                              Math.pow(pixelLeft.getBlue() - pixelRight.getBlue(), 2);
        double deltaYSquare = Math.pow(pixelTop.getRed() - pixelBot.getRed(), 2) +
                              Math.pow(pixelTop.getGreen() - pixelBot.getGreen(), 2) +
                              Math.pow(pixelTop.getBlue() - pixelBot.getBlue(), 2);

        return Math.sqrt(deltaXSquare + deltaYSquare);

    }

    public int[] findHorizontalSeam() {
        return findSeam(true);
    }

    public int[] findVerticalSeam() {
        return findSeam(false);
    }

    private int[] findSeam(boolean horizontal) {
        PixelGraph graph = new PixelGraph(horizontal);
        Pixel start = getStart(graph, horizontal);
        Pixel end = getEnd(graph, horizontal);

        ShortestPathsSolver<Pixel> solver = new AStarSolver<>(graph, start, end, 10);
        List<Pixel> solution = solver.solution();

        int[] result = new int[solution.size() - 2];
        for (int i = 0; i < solution.size() - 2; i++) {
            if (horizontal) {
                result[i] = solution.get(i + 1).y();
            } else {
                result[i] = solution.get(i + 1).x();
            }
        }

        return result;
    }

    private Pixel getStart(PixelGraph graph, boolean horizontal) {
        Pixel start = new Pixel(-1, -1, 0);
        if (horizontal) {
            for (int i = 0; i < graph.energies[0].length; i++) {
                start.addNeighbors(graph.energies[0][i]);
            }
        } else {
            for (int i = 0; i < graph.energies.length; i++) {
                start.addNeighbors(graph.energies[i][0]);
            }
        }

        return start;
    }

    private Pixel getEnd(PixelGraph graph, boolean horizontal) {
        Pixel end = new Pixel(-1, -1, 0);
        if (horizontal) {
            for (int i = 0; i < graph.energies[0].length; i++) {
                graph.energies[width() - 1][i].addNeighbors(end);
            }
        } else {
            for (int i = 0; i < graph.energies.length; i++) {
                graph.energies[i][height() - 1].addNeighbors(end);
            }
        }
        return end;
    }

    private class PixelGraph implements AStarGraph<Pixel> {

        public Pixel[][] energies;

        public PixelGraph(boolean horizontal) {

            // save picture by swapping x, y pixels
            energies = new Pixel[width()][height()];

            for (int x = 0; x < energies.length; x++) {
                for (int y = 0; y < energies[0].length; y++){
                    energies[x][y] = new Pixel(x, y);
                }
            }

            if (horizontal) {
                for (int x = 0; x < energies.length - 1; x++) {
                    for (int y = 0; y < energies[0].length; y++) {

                        energies[x][y].energy(energy(x, y));
                        energies[x][y].addNeighbors(energies[x + 1][y]);
                        if (y != 0) {
                            energies[x][y].addNeighbors(energies[x + 1][y - 1]);
                        }

                        if (y != energies[0].length - 1) {
                            energies[x][y].addNeighbors(energies[x + 1][y + 1]);
                        }
                        // calculate last x energy
                        energies[energies.length - 1][y].energy(energy(energies.length - 1, y));
                    }
                }
            } else {
                for (int x = 0; x < energies.length; x++) {
                    for (int y = 0; y < energies[0].length - 1; y++){

                        energies[x][y].energy(energy(x, y));
                        energies[x][y].addNeighbors(energies[x][y + 1]);
                        if (x != 0) {
                            energies[x][y].addNeighbors(energies[x - 1][y + 1]);
                        }

                        if (x != energies.length - 1) {
                            energies[x][y].addNeighbors(energies[x + 1][y + 1]);
                        }
                        // calculate last y energy
                        energies[x][energies[0].length - 1].energy(energy(x, energies[0].length - 1));
                    }
                }
            }
        }

        @Override
        public List<WeightedEdge<Pixel>> neighbors(Pixel v) {
            List<Pixel> neighbors = v.neighbors;
            List<WeightedEdge<Pixel>> neighborEdges = new ArrayList<>();
            for (Pixel neighbor: neighbors) {
                neighborEdges.add(new WeightedEdge<>(v, neighbor, neighbor.energy));
            }
            return neighborEdges;
        }

        @Override
        public double estimatedDistanceToGoal(Pixel s, Pixel goal) {
            return 0;
        }
    }

    private class Pixel {

        private int x;
        private int y;
        private double energy;
        private List<Pixel> neighbors;

        public Pixel(int x, int y) {
            this(x, y, 0);
        }

        public Pixel(int x, int y, double energy) {
            this.x = x;
            this.y = y;
            this.energy = energy;
            neighbors = new ArrayList<>();
        }

        public int x() { return x; }

        public int y() { return y; }

        public void energy(double newEnergy) { this.energy = newEnergy; }

        public void addNeighbors(Pixel p) {
            neighbors.add(p);
        }

        public List<Pixel> getNeighbors() { return neighbors; }

        public String toString() {
            return String.format("(%d, %d, %.4f)", x, y, energy);
        }
    }
}
