package io.jzheaux.pluralsight.securecoding.module5;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.function.BiFunction;
import java.util.zip.ZipFile;

import static org.junit.jupiter.api.Assertions.fail;

public class MusicTests {
	@Test
	@Disabled
	public void testZipBombs() throws Exception {
		byte[] buffer = new byte[1024];
		long limit = 30000000;
		CountingOutputStream output = new CountingOutputStream();
		ZipFile zip = new ZipFile(new File("evil.zip"));
		zip.stream().forEach((entry) -> {
			try {
				int read;
				InputStream input = zip.getInputStream(entry);
				while ((read = input.read(buffer, 0, buffer.length)) != -1) {
					if (output.count + read > limit) {
						throw new IllegalArgumentException("too big!");
					}
					output.write(buffer, 0, read);
				}
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		});
		System.out.println(output.getCount());
	}

	private static class CountingOutputStream extends OutputStream {
		private long count;

		@Override
		public void write(int b) throws IOException {
			this.count++;
		}

		long getCount() {
			return this.count;
		}
	}

	@Test
	public void failingCheck() {
		BiFunction<Number, Integer, Boolean> check =
				(value, step) -> (int) value + step < Integer.MAX_VALUE;
		doIncrement(1_000_000_000, check);
		doIncrement(2_349_742, check);
		doIncrement(4534, check);
	}

	@Test
	public void subtractionCheck() {
		BiFunction<Number, Integer, Boolean> check =
				(value, step) -> Integer.MAX_VALUE - (int) value > step;
		doIncrement(1_000_000_000, check);
		doIncrement(2_349_742, check);
		doIncrement(4534, check);
	}

	@Test
	public void widerTypeCheck() {
		BiFunction<Number, Integer, Boolean> check =
				(value, step) -> value.longValue() + step < Integer.MAX_VALUE;
		doIncrement(1_000_000_000, check);
		doIncrement(2_349_742, check);
		doIncrement(4534, check);
	}

	@Test
	public void rangeCheck() {
		BiFunction<Number, Integer, Boolean> check = (value, step) -> {
			int result = (int) value + step;
			return result > 0 && result < Integer.MAX_VALUE;
		};
		doIncrement(1_000_000_000, check);
		doIncrement(2_349_742, check);
		doIncrement(4534, check);
	}

	private void doIncrement(int step, BiFunction<Number, Integer, Boolean> check) {
		for (int i = 0; check.apply(i, step); i += step) {
			if (i < 0) {
				fail("Overflow not detected!");
			}
		}
	}
}
