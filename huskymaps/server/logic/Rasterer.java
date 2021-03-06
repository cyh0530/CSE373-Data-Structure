package huskymaps.server.logic;

import huskymaps.params.RasterRequest;
import huskymaps.params.RasterResult;

import java.util.Objects;

import static huskymaps.utils.Constants.NUM_X_TILES_AT_DEPTH;
import static huskymaps.utils.Constants.NUM_Y_TILES_AT_DEPTH;
import static huskymaps.utils.Constants.ROOT_LON_DIFF;
import static huskymaps.utils.Constants.ROOT_LAT_DIFF;
import static huskymaps.utils.Constants.ROOT_ULLON;
import static huskymaps.utils.Constants.ROOT_ULLAT;
import static huskymaps.utils.Constants.MIN_X_TILE_AT_DEPTH;
import static huskymaps.utils.Constants.MIN_Y_TILE_AT_DEPTH;
import static huskymaps.utils.Constants.MIN_ZOOM_LEVEL;

/** Application logic for the RasterAPIHandler. */
public class Rasterer {

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param request RasterRequest
     * @return RasterResult
     */
    public static RasterResult rasterizeMap(RasterRequest request) {
        int depth = request.depth;
        int xTiles = NUM_X_TILES_AT_DEPTH[depth];
        int yTiles = NUM_Y_TILES_AT_DEPTH[depth];
        double xTileWidth = ROOT_LON_DIFF / xTiles;
        double yTileWidth = ROOT_LAT_DIFF / yTiles;
        int ullon = (int) (Math.abs(request.ullon - ROOT_ULLON) / xTileWidth);
        int ullat = (int) (Math.abs(request.ullat - ROOT_ULLAT) / yTileWidth);
        int lrlon = (int) (Math.abs(request.lrlon - ROOT_ULLON) / xTileWidth);
        int lrlat = (int) (Math.abs(request.lrlat - ROOT_ULLAT) / yTileWidth);

        Tile[][] grid = new Tile[lrlat - ullat + 1][lrlon - ullon + 1];

        for (int i = 0; i < lrlat - ullat + 1; i++) {
            for (int j = 0; j < lrlon - ullon + 1; j++) {
                grid[i][j] = new Tile(depth, ullon + j, ullat + i);
            }
        }
        return new RasterResult(grid);
    }

    public static class Tile {
        public final int depth;
        public final int x;
        public final int y;

        public Tile(int depth, int x, int y) {
            this.depth = depth;
            this.x = x;
            this.y = y;
        }

        public Tile offset() {
            return new Tile(depth, x + 1, y + 1);
        }

        /**
         * Return the latitude of the upper-left corner of the given slippy map tile.
         * @return latitude of the upper-left corner
         * @source https://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
         */
        public double lat() {
            double n = Math.pow(2.0, MIN_ZOOM_LEVEL + depth);
            int slippyY = MIN_Y_TILE_AT_DEPTH[depth] + y;
            double latRad = Math.atan(Math.sinh(Math.PI * (1 - 2 * slippyY / n)));
            return Math.toDegrees(latRad);
        }

        /**
         * Return the longitude of the upper-left corner of the given slippy map tile.
         * @return longitude of the upper-left corner
         * @source https://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
         */
        public double lon() {
            double n = Math.pow(2.0, MIN_ZOOM_LEVEL + depth);
            int slippyX = MIN_X_TILE_AT_DEPTH[depth] + x;
            return slippyX / n * 360.0 - 180.0;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Tile tile = (Tile) o;
            return depth == tile.depth &&
                    x == tile.x &&
                    y == tile.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(depth, x, y);
        }

        @Override
        public String toString() {
            return "d" + depth + "_x" + x + "_y" + y + ".jpg";
        }
    }
}
