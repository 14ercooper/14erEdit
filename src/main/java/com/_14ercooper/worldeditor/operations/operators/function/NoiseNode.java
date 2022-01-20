/**
 * This file is part of 14erEdit.
 *
  * 14erEdit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * 14erEdit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with 14erEdit.  If not, see <https://www.gnu.org/licenses/>.
 */

package com._14ercooper.worldeditor.operations.operators.function;

import com._14ercooper.worldeditor.main.FastNoise;
import com._14ercooper.worldeditor.main.FastNoise.CellularDistanceFunction;
import com._14ercooper.worldeditor.main.FastNoise.FractalType;
import com._14ercooper.worldeditor.main.SimplexNoise;
import com._14ercooper.worldeditor.operations.DummyState;
import com._14ercooper.worldeditor.operations.OperatorState;
import com._14ercooper.worldeditor.operations.Parser;
import com._14ercooper.worldeditor.operations.ParserState;
import com._14ercooper.worldeditor.operations.operators.Node;
import com._14ercooper.worldeditor.operations.operators.core.NumberNode;
import com._14ercooper.worldeditor.operations.operators.core.StringNode;

public class NoiseNode extends Node {

    public StringNode noiseType; // What type of noise?
    public NumberNode dimensions; // How many dimensions (most 2d/3d, simplex/white 4d also)
    public NumberNode cutoff; // Cutoff for true
    public NumberNode frequency; // Coarseness of output
    public NumberNode octaves, lacunarity, gain, type; // Used by fractals
    public StringNode distance; // Used by cellular
    private FastNoise noise;

    // Valid inputs for noiseType:
    // Value ValueFractal Perlin PerlinFractal Simplex SimplexFractal Cellular
    // WhiteNoise WhiteNoiseInt Cubic CubicFractal
    // Valid distances for cellular are: Euclid Manhattan Natural

    // /fx br s 7 0.5 ? both bedrock ## cellular 2 140 4 natural set stone

    @Override
    public NoiseNode newNode(ParserState parserState) {
        NoiseNode node = new NoiseNode();
        node.noiseType = Parser.parseStringNode(parserState);
        node.dimensions = Parser.parseNumberNode(parserState);
        node.cutoff = Parser.parseNumberNode(parserState);
        node.frequency = Parser.parseNumberNode(parserState);

        node.noise = new FastNoise();
        node.noise.SetSeed(SimplexNoise.noiseSeed);
        node.noise.SetFrequency((float) (node.frequency.getValue(new DummyState(parserState.getCurrentPlayer())) / 40.0));

        if (node.noiseType.getText().contains("Fractal") || node.noiseType.getText().contains("fractal")) {
            node.octaves = Parser.parseNumberNode(parserState);
            node.lacunarity = Parser.parseNumberNode(parserState);
            node.gain = Parser.parseNumberNode(parserState);
            node.type = Parser.parseNumberNode(parserState);
            node.noise.SetFractalOctaves((int) node.octaves.getValue(new DummyState(parserState.getCurrentPlayer())));
            node.noise.SetFractalLacunarity((float) node.lacunarity.getValue(new DummyState(parserState.getCurrentPlayer())));
            node.noise.SetFractalGain((float) node.gain.getValue(new DummyState(parserState.getCurrentPlayer())));
            int fractalType = (int) node.type.getValue(new DummyState(parserState.getCurrentPlayer()));
            if (fractalType == 1) {
                node.noise.SetFractalType(FractalType.FBM);
            } else if (fractalType == 2) {
                node.noise.SetFractalType(FractalType.Billow);
            } else if (fractalType == 3) {
                node.noise.SetFractalType(FractalType.RigidMulti);
            }
        }

        if (node.noiseType.getText().equalsIgnoreCase("cellular")) {
            node.distance = Parser.parseStringNode(parserState);
            if (node.distance.getText().equalsIgnoreCase("euclid")) {
                node.noise.SetCellularDistanceFunction(CellularDistanceFunction.Euclidean);
            } else if (node.distance.getText().equalsIgnoreCase("manhattan")) {
                node.noise.SetCellularDistanceFunction(CellularDistanceFunction.Manhattan);
            } else if (node.distance.getText().equalsIgnoreCase("natural")) {
                node.noise.SetCellularDistanceFunction(CellularDistanceFunction.Natural);
            }
        }

        return node;
    }

    @Override
    public boolean performNode(OperatorState state, boolean perform) {
        return scaleTo255(getNum(state)) <= cutoff.getValue(state);
    }

    public float getNum(OperatorState state) {
        int x = state.getCurrentBlock().block.getX();
        int y = state.getCurrentBlock().block.getY();
        int z = state.getCurrentBlock().block.getZ();
        int w = (int) ((x + y + z) / 0.33333333);
        int dim = (int) dimensions.getValue(state);

        if (noiseType.getText().equalsIgnoreCase("value")) {
            if (dim == 2) {
                return noise.GetValue(x, z);
            } else if (dim == 3) {
                return noise.GetValue(x, z, y);
            } else {
                return 0;
            }
        } else if (noiseType.getText().equalsIgnoreCase("valuefractal")) {
            if (dim == 2) {
                return noise.GetValueFractal(x, z);
            } else if (dim == 3) {
                return noise.GetValueFractal(x, z, y);
            } else {
                return 0;
            }
        } else if (noiseType.getText().equalsIgnoreCase("perlin")) {
            if (dim == 2) {
                return noise.GetPerlin(x, z);
            } else if (dim == 3) {
                return noise.GetPerlin(x, z, y);
            } else {
                return 0;
            }
        } else if (noiseType.getText().equalsIgnoreCase("perlinfractal")) {
            if (dim == 2) {
                return noise.GetPerlinFractal(x, z);
            } else if (dim == 3) {
                return noise.GetPerlinFractal(x, z, y);
            } else {
                return 0;
            }
        } else if (noiseType.getText().equalsIgnoreCase("simplex")) {
            if (dim == 2) {
                return noise.GetSimplex(x, z);
            } else if (dim == 3) {
                return noise.GetSimplex(x, z, y);
            } else if (dim == 4) {
                return noise.GetSimplex(x, z, y, w);
            } else {
                return 0;
            }
        } else if (noiseType.getText().equalsIgnoreCase("simplexfractal")) {
            if (dim == 2) {
                return noise.GetSimplexFractal(x, z);
            } else if (dim == 3) {
                return noise.GetSimplexFractal(x, z, y);
            } else {
                return 0;
            }
        } else if (noiseType.getText().equalsIgnoreCase("cellular")) {
            if (dim == 2) {
                return noise.GetCellular(x, z);
            } else if (dim == 3) {
                return noise.GetCellular(x, z, y);
            } else {
                return 0;
            }
        } else if (noiseType.getText().equalsIgnoreCase("whitenoise") || noiseType.getText().equalsIgnoreCase("whitenoiseint")) {
            if (dim == 2) {
                return noise.GetWhiteNoise(x, z);
            } else if (dim == 3) {
                return noise.GetWhiteNoise(x, z, y);
            } else if (dim == 4) {
                return noise.GetWhiteNoise(x, z, y, w);
            } else {
                return 0;
            }
        } else if (noiseType.getText().equalsIgnoreCase("cubic")) {
            if (dim == 2) {
                return noise.GetCubic(x, z);
            } else if (dim == 3) {
                return noise.GetCubic(x, z, y);
            } else {
                return 0;
            }
        } else if (noiseType.getText().equalsIgnoreCase("cubicfractal")) {
            if (dim == 2) {
                return noise.GetCubicFractal(x, z);
            } else if (dim == 3) {
                return noise.GetCubicFractal(x, z, y);
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    @Override
    public int getArgCount() {
        return 3;
    }

    public static float scaleTo255(double val) {
        val = val + 1;
        val = val * 127.5;
        return (float) val;
    }
}
