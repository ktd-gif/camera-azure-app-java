package com.microsoft.camera.cameraApp.benchmarks;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
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
 * Measures the whole local capture path, from the frame handed over by the grabber to the payload
 * that is uploaded to blob storage: frame conversion, JPEG encoding and buffer preparation.
 *
 * <p>The network call itself is intentionally left out so that the benchmark stays deterministic and
 * only reflects the CPU work done by the app.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(3)
@State(Scope.Benchmark)
public class CapturePipelineBenchmark {

	@Param({ "640x480", "1280x720" })
	private String resolution;

	private Frame frame;

	@Setup
	public void setup() {
		int[] size = BenchmarkImages.parseResolution(resolution);
		frame = BenchmarkImages.syntheticFrame(size[0], size[1]);
	}

	@Benchmark
	public ByteBuffer captureToUploadPayload() {
		BufferedImage image = ImageUtils.toBufferedImage(frame);
		byte[] encoded = ImageUtils.imageToByes(image);
		return ByteBuffer.wrap(encoded);
	}
}
