package io.jzheaux.pluralsight.securecoding.module5;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Controller("module5AppController")
public class AppController {
	private final Log logger = LogFactory.getLog(this.getClass());

	private final MusicRepository music;

	private final Function<String, OutputStream> storage;

	private final Map<String, Long> sizes = new LinkedHashMap<>();

	@Autowired
	public AppController(MusicRepository music) {
		this(music, AppController::location);
	}

	public AppController(MusicRepository music, Function<String, OutputStream> storage) {
		this.music = music;
		this.storage = storage;
	}

	private static OutputStream location(String location) {
		try {
			File parent = new File("tracks");
			parent.mkdirs();
			return new FileOutputStream(new File(parent, location));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	@GetMapping("/music")
	String music(Model model) {
		model.addAttribute("music", this.music.findAll());
		return "music";
	}

	@PostMapping("/music")
	String music(@RequestParam("file") MultipartFile zipped, Model model) {
		try {
			byte[] buffer = new byte[1024];
			ZipEntry entry;
			int read;
			ZipInputStream zip = new ZipInputStream(zipped.getInputStream());
			while ((entry = zip.getNextEntry()) != null) {
				String filename = UUID.randomUUID().toString();
				String title = entry.getName();
				OutputStream file = this.storage.apply(filename);
				while ((read = zip.read(buffer, 0, buffer.length)) != -1) {
					file.write(buffer, 0, read);
				}
				file.close();
				this.sizes.put(title.substring(0, Math.min(title.length(), 64)), entry.getSize());
				this.music.save(new Music(title, filename));
			}
		} catch (IOException ex) {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
		}
		return "redirect:/music";
	}
}
