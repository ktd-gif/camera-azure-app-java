package com.microsoft.camera.cameraApp.benchmarks;

import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

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
 * Measures the JPEG encoding of a captured picture.
 *
 * <p>{@code ImageUtils.imageToByes} runs for every picture taken in the UI, right before the bytes
 * are handed over to the blob storage manager, which makes it the hottest CPU bound step of the
 * capture path.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(3)
@State(Scope.Benchmark)
public class ImageEncodingBenchmark {

	@Param({ "320x240", "640x480", "1280x720" })
	private String resolution;

	private BufferedImage image;

	@Setup
	public void setup() {
		int[] size = BenchmarkImages.parseResolution(resolution);
		image = BenchmarkImages.syntheticImage(size[0], size[1]);
	}

	@Benchmark
	public byte[] encodeToJpeg() {
		return ImageUtils.imageToByes(image);
	}
}
