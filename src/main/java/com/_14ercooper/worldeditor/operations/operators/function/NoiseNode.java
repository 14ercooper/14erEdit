package com._14ercooper.worldeditor.operations.operators.function;

import com._14ercooper.worldeditor.main.FastNoise;
import com._14ercooper.worldeditor.main.GlobalVars;
import com._14ercooper.worldeditor.main.FastNoise.CellularDistanceFunction;
import com._14ercooper.worldeditor.main.FastNoise.FractalType;
import com._14ercooper.worldeditor.operations.Operator;
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

    // Valid inputs for noiseType:
    // Value ValueFractal Perlin PerlinFractal Simplex SimplexFractal Cellular WhiteNoise WhiteNoiseInt Cubic CubicFractal Perturb PerturbFractal
    // Valid distances for cellular are: Euclid Manhattan Natural
    
    // /fx br s 7 0.5 ? both bedrock ## cellular 2 140 4 natural set stone
    
    public NoiseNode newNode() {
	NoiseNode node = new NoiseNode();
	node.noiseType = GlobalVars.operationParser.parseStringNode();
	node.dimensions = GlobalVars.operationParser.parseNumberNode();
	node.cutoff = GlobalVars.operationParser.parseNumberNode();
	node.frequency = GlobalVars.operationParser.parseNumberNode();
	
	if (node.noiseType.getText().contains("Fractal") || node.noiseType.getText().contains("fractal")) {
	    node.octaves = GlobalVars.operationParser.parseNumberNode();
	    node.lacunarity = GlobalVars.operationParser.parseNumberNode();
	    node.gain = GlobalVars.operationParser.parseNumberNode();
	    node.type = GlobalVars.operationParser.parseNumberNode();
	}
	
	if (node.noiseType.getText().equalsIgnoreCase("cellular")) {
	    node.distance = GlobalVars.operationParser.parseStringNode();
	}
	
	return node;
    }
    
    public boolean performNode() {
	return scaleTo255(getNum()) <= cutoff.getValue();
    }

    public float getNum() {
	int x = Operator.currentBlock.getX();
	int y = Operator.currentBlock.getY();
	int z = Operator.currentBlock.getZ();
	int w = (int) ((x + y + z) / 0.33333333);
	float freq = (float) (frequency.getValue() / 40.0);
	int dim = (int) dimensions.getValue();
	
	if (noiseType.getText().equalsIgnoreCase("value")) {
	    FastNoise noise = new FastNoise();
	    noise.SetSeed(GlobalVars.noiseSeed);
	    noise.SetFrequency(freq);
	    if (dim == 2) {
		return noise.GetValue(x, z);
	    }
	    else if (dim == 3) {
		return noise.GetValue(x, z, y);
	    }
	    else {
		return 0;
	    }
	}
	else if (noiseType.getText().equalsIgnoreCase("valuefractal")) {
	    FastNoise noise = new FastNoise();
	    noise.SetSeed(GlobalVars.noiseSeed);
	    noise.SetFrequency(freq);
	    noise.SetFractalOctaves((int) octaves.getValue());
	    noise.SetFractalLacunarity((float) lacunarity.getValue());
	    noise.SetFractalGain((float) gain.getValue());
	    int fractalType = (int) type.getValue();
	    if (fractalType == 1) {
		noise.SetFractalType(FractalType.FBM);
	    }
	    else if (fractalType == 2) {
		noise.SetFractalType(FractalType.Billow);
	    }
	    else if (fractalType == 3) {
		noise.SetFractalType(FractalType.RigidMulti);
	    }
	    if (dim == 2) {
		return noise.GetValueFractal(x, z);
	    }
	    else if (dim == 3) {
		return noise.GetValueFractal(x, z, y);
	    }
	    else {
		return 0;
	    }
	}
	else if (noiseType.getText().equalsIgnoreCase("perlin")) {
	    FastNoise noise = new FastNoise();
	    noise.SetSeed(GlobalVars.noiseSeed);
	    noise.SetFrequency(freq);
	    if (dim == 2) {
		return noise.GetPerlin(x, z);
	    }
	    else if (dim == 3) {
		return noise.GetPerlin(x, z, y);
	    }
	    else {
		return 0;
	    }
	}
	else if (noiseType.getText().equalsIgnoreCase("perlinfractal")) {
	    FastNoise noise = new FastNoise();
	    noise.SetSeed(GlobalVars.noiseSeed);
	    noise.SetFrequency(freq);
	    noise.SetFractalOctaves((int) octaves.getValue());
	    noise.SetFractalLacunarity((float) lacunarity.getValue());
	    noise.SetFractalGain((float) gain.getValue());
	    int fractalType = (int) type.getValue();
	    if (fractalType == 1) {
		noise.SetFractalType(FractalType.FBM);
	    }
	    else if (fractalType == 2) {
		noise.SetFractalType(FractalType.Billow);
	    }
	    else if (fractalType == 3) {
		noise.SetFractalType(FractalType.RigidMulti);
	    }
	    if (dim == 2) {
		return noise.GetPerlinFractal(x, z);
	    }
	    else if (dim == 3) {
		return noise.GetPerlinFractal(x, z, y);
	    }
	    else {
		return 0;
	    }
	}
	else if (noiseType.getText().equalsIgnoreCase("simplex")) {
	    FastNoise noise = new FastNoise();
	    noise.SetSeed(GlobalVars.noiseSeed);
	    noise.SetFrequency(freq);
	    if (dim == 2) {
		return noise.GetSimplex(x, z);
	    }
	    else if (dim == 3) {
		return noise.GetSimplex(x, z, y);
	    }
	    else if (dim == 4) {
		return noise.GetSimplex(x, z, y, w);
	    }
	    else {
		return 0;
	    }
	}
	else if (noiseType.getText().equalsIgnoreCase("simplexfractal")) {
	    FastNoise noise = new FastNoise();
	    noise.SetSeed(GlobalVars.noiseSeed);
	    noise.SetFrequency(freq);
	    noise.SetFractalOctaves((int) octaves.getValue());
	    noise.SetFractalLacunarity((float) lacunarity.getValue());
	    noise.SetFractalGain((float) gain.getValue());
	    int fractalType = (int) type.getValue();
	    if (fractalType == 1) {
		noise.SetFractalType(FractalType.FBM);
	    }
	    else if (fractalType == 2) {
		noise.SetFractalType(FractalType.Billow);
	    }
	    else if (fractalType == 3) {
		noise.SetFractalType(FractalType.RigidMulti);
	    }
	    if (dim == 2) {
		return noise.GetSimplexFractal(x, z);
	    }
	    else if (dim == 3) {
		return noise.GetSimplexFractal(x, z, y);
	    }
	    else {
		return 0;
	    }
	}
	else if (noiseType.getText().equalsIgnoreCase("cellular")) {
	    FastNoise noise = new FastNoise();
	    noise.SetSeed(GlobalVars.noiseSeed);
	    noise.SetFrequency(freq);
	    if (distance.getText().equalsIgnoreCase("euclid")) {
		noise.SetCellularDistanceFunction(CellularDistanceFunction.Euclidean);
	    }
	    else if (distance.getText().equalsIgnoreCase("manhattan")) {
		noise.SetCellularDistanceFunction(CellularDistanceFunction.Manhattan);
	    }
	    else if (distance.getText().equalsIgnoreCase("natural")) {
		noise.SetCellularDistanceFunction(CellularDistanceFunction.Natural);
	    }
	    if (dim == 2) {
		return noise.GetCellular(x, z);
	    }
	    else if (dim == 3) {
		return noise.GetCellular(x, z, y);
	    }
	    else {
		return 0;
	    }
	}
	else if (noiseType.getText().equalsIgnoreCase("whitenoise")) {
	    FastNoise noise = new FastNoise();
	    noise.SetSeed(GlobalVars.noiseSeed);
	    noise.SetFrequency(freq);
	    if (dim == 2) {
		return noise.GetWhiteNoise(x, z);
	    }
	    else if (dim == 3) {
		return noise.GetWhiteNoise(x, z, y);
	    }
	    else if (dim == 4) {
		return noise.GetWhiteNoise(x, z, y, w);
	    }
	    else {
		return 0;
	    }
	}
	else if (noiseType.getText().equalsIgnoreCase("whitenoiseint")) {
	    FastNoise noise = new FastNoise();
	    noise.SetSeed(GlobalVars.noiseSeed);
	    noise.SetFrequency(freq);
	    if (dim == 2) {
		return noise.GetWhiteNoise(x, z);
	    }
	    else if (dim == 3) {
		return noise.GetWhiteNoise(x, z, y);
	    }
	    else if (dim == 4) {
		return noise.GetWhiteNoise(x, z, y, w);
	    }
	    else {
		return 0;
	    }
	}
	else if (noiseType.getText().equalsIgnoreCase("cubic")) {
	    FastNoise noise = new FastNoise();
	    noise.SetSeed(GlobalVars.noiseSeed);
	    noise.SetFrequency(freq);
	    if (dim == 2) {
		return noise.GetCubic(x, z);
	    }
	    else if (dim == 3) {
		return noise.GetCubic(x, z, y);
	    }
	    else {
		return 0;
	    }
	}
	else if (noiseType.getText().equalsIgnoreCase("cubicfractal")) {
	    FastNoise noise = new FastNoise();
	    noise.SetSeed(GlobalVars.noiseSeed);
	    noise.SetFrequency(freq);
	    noise.SetFractalOctaves((int) octaves.getValue());
	    noise.SetFractalLacunarity((float) lacunarity.getValue());
	    noise.SetFractalGain((float) gain.getValue());
	    int fractalType = (int) type.getValue();
	    if (fractalType == 1) {
		noise.SetFractalType(FractalType.FBM);
	    }
	    else if (fractalType == 2) {
		noise.SetFractalType(FractalType.Billow);
	    }
	    else if (fractalType == 3) {
		noise.SetFractalType(FractalType.RigidMulti);
	    }
	    if (dim == 2) {
		return noise.GetCubicFractal(x, z);
	    }
	    else if (dim == 3) {
		return noise.GetCubicFractal(x, z, y);
	    }
	    else {
		return 0;
	    }
	}
	else {
	    return 0;
	}
    }

    public int getArgCount() {
	return 3;
    }
    
    private float scaleTo255 (double val) {
	val = val + 1;
	val = val * 127.5;
	return (float) val;
    }
}
