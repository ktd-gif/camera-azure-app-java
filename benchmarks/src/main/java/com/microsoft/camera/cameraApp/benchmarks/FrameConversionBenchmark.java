package com.microsoft.camera.cameraApp.benchmarks;

import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import org.bytedeco.javacv.Frame;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import com.microsoft.camera.cameraApp.utils.ImageUtils;

/**
 * Measures the conversion of a grabbed camera frame into an AWT image.
 *
 * <p>{@code ImageUtils.toBufferedImage} is called for every frame that is displayed or captured, and
 * it allocates a new {@code Java2DFrameConverter} on each call, so the conversion cost is worth
 * tracking over time.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(3)
@State(Scope.Benchmark)
public class FrameConversionBenchmark {

	@Param({ "320x240", "640x480", "1280x720" })
	private String resolution;

	private Frame frame;

	@Setup
	public void setup() {
		int[] size = BenchmarkImages.parseResolution(resolution);
		frame = BenchmarkImages.syntheticFrame(size[0], size[1]);
	}

	@Benchmark
	public BufferedImage frameToBufferedImage() {
		return ImageUtils.toBufferedImage(frame);
	}
}
