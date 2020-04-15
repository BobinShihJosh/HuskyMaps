package huskymaps.rastering;

import huskymaps.graph.Coordinate;
import huskymaps.utils.Constants;

/**
 * @see Rasterer
 */
public class DefaultRasterer implements Rasterer {
    public TileGrid rasterizeMap(Coordinate ul, Coordinate lr, int depth) {

        Tile[][] grid;
        // Edge Case 1 : Query box extends past at least one of the edges of the give map
        if (ul.lon() <= Constants.ROOT_ULLON || ul.lat() >= Constants.ROOT_ULLAT
            || lr.lon() >= Constants.ROOT_LRLON || lr.lat() <= Constants.ROOT_ULLON) {

            // edge case arbitrary val given at middle of map
            grid = new Tile[(int) Constants.ROOT_LAT][(int) Constants.ROOT_LON];
            grid[0][0] = new Tile(depth, 0, 0);

        } else {
            // get the upper left and lower right coordinates tile numbers
            int ulX = (int) ((ul.lon() - Constants.ROOT_ULLON) / Constants.LON_PER_TILE[depth]);
            int ulY = (int) ((Constants.ROOT_ULLAT - ul.lat()) / Constants.LAT_PER_TILE[depth]);
            int lrX = (int) ((lr.lon() - Constants.ROOT_ULLON) / Constants.LON_PER_TILE[depth]);
            int lrY = (int) ((Constants.ROOT_ULLAT - lr.lat()) / Constants.LAT_PER_TILE[depth]);
            // check if any of these are out of bounds and fix them
            if (ulX < 0) {
                ulX = 0;
            }
            if (ulY < 0) {
                ulY = 0;
            }
            if (lrX >= Constants.NUM_X_TILES_AT_DEPTH[depth]) {
                lrX = Constants.NUM_X_TILES_AT_DEPTH[depth] - 1;
            }
            if (lrY >= Constants.NUM_Y_TILES_AT_DEPTH[depth]) {
                lrY = Constants.NUM_Y_TILES_AT_DEPTH[depth] - 1;
            }
            // Get the x and y range of the image
            int xRange = lrX - ulX + 1;
            int yRange = lrY - ulY + 1;
            grid = new Tile[yRange][xRange];
            for (int i = 0; i < yRange; i++) {
                for (int j = 0; j < xRange; j++) {
                    grid[i][j] = new Tile(depth, ulX + j, ulY + i);
                }
            }
        }
        return new TileGrid(grid);
    }
}
