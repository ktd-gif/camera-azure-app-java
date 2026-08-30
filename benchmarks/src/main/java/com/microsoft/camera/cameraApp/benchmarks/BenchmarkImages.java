package com.microsoft.camera.cameraApp.benchmarks;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.bytedeco.javacv.Frame;

/**
 * Helpers building deterministic, photo-like inputs for the benchmarks.
 *
 * <p>The generated content mixes smooth gradients with high frequency noise so that JPEG encoding
 * has a realistic amount of work to do: a uniform image would compress in a fraction of the time and
 * would not be representative of an actual camera capture.
 */
final class BenchmarkImages {

	private BenchmarkImages() {
	}

	/** Parses a resolution given as {@code <width>x<height>}. */
	static int[] parseResolution(String resolution) {
		int separator = resolution.indexOf('x');
		int width = Integer.parseInt(resolution.substring(0, separator));
		int height = Integer.parseInt(resolution.substring(separator + 1));
		return new int[] { width, height };
	}

	/** Builds a photo-like image in the format produced by the camera pipeline. */
	static BufferedImage syntheticImage(int width, int height) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int blue = channel(x, y, 0, width, height);
				int green = channel(x, y, 1, width, height);
				int red = channel(x, y, 2, width, height);
				image.setRGB(x, y, (red << 16) | (green << 8) | blue);
			}
		}
		return image;
	}

	/** Builds a javacv {@link Frame} as the grabber hands it over to the app. */
	static Frame syntheticFrame(int width, int height) {
		Frame frame = new Frame(width, height, Frame.DEPTH_UBYTE, 3);
		ByteBuffer buffer = (ByteBuffer) frame.image[0];
		int stride = frame.imageStride;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int offset = y * stride + x * 3;
				buffer.put(offset, (byte) channel(x, y, 0, width, height));
				buffer.put(offset + 1, (byte) channel(x, y, 1, width, height));
				buffer.put(offset + 2, (byte) channel(x, y, 2, width, height));
			}
		}
		buffer.rewind();
		return frame;
	}

	private static int channel(int x, int y, int index, int width, int height) {
		int gradient = ((x * 255) / width + (y * 255) / height + index * 40) / 2;
		return clamp(gradient + (noise(x, y + index * 977) % 48) - 24);
	}

	private static int noise(int x, int y) {
		int hash = (x * 73856093) ^ (y * 19349663);
		hash ^= hash >>> 13;
		hash *= 1274126177;
		return Math.abs(hash ^ (hash >>> 16));
	}

	private static int clamp(int value) {
		if (value < 0) {
			return 0;
		}
		return value > 255 ? 255 : value;
	}
}
