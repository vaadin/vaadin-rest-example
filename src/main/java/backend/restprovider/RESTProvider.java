package backend.restprovider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import org.apache.commons.lang3.text.WordUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Implementation of a paging REST provider. Used to demonstrate lazy loading
 * data providers.
 * <p>
 * Created mainly because most 3rd party REST APIs are commercial, especially
 * for the amount of traffic a Vaadin demo needs.
 * <p>
 * The implementation here is not relevant for the Vaadin example itself, and
 * should not be taken as a production-ready implementation for REST services.
 */
@RestController
public class RESTProvider {

	private static final int MAX_COUNT = 500;
	private static final int SIZE = 10000;

	/**
	 * Random words used for generating data
	 */
	private static final String[] words;
	static {

		final String LOREM_IPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer non vulputate elit, a aliquam nisi. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Cras sagittis mauris varius dolor varius pulvinar. Integer ac suscipit ligula, non lacinia est. Nulla metus velit, vulputate sed sagittis at, semper quis turpis. Proin in metus vestibulum, luctus mauris nec, ultrices ipsum. Etiam maximus massa porta congue tempor. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Cras ut justo sed purus pretium egestas. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Vestibulum rutrum vitae ex sed rutrum. Donec eget metus eget lacus rhoncus interdum sed sed urna.\n"
				+ "Cras blandit sapien leo, vel ultrices justo scelerisque posuere. Maecenas ut sollicitudin ante. Ut euismod mauris a laoreet tristique. Donec tristique risus a ornare feugiat. Proin et condimentum risus. Vivamus accumsan, nisl ut interdum interdum, purus massa bibendum enim, in lobortis purus ex ac mi. Aenean tempus odio urna, eu aliquam neque sodales id. Aenean magna ante, ultrices sed vestibulum nec, facilisis eget leo. Duis erat elit, rhoncus ac leo nec, tincidunt facilisis augue.";

		words = LOREM_IPSUM.split(" ");
	}

	private final Random rand = new Random();

	/**
	 * Replacement for a DB. Is lazily populated by demand.
	 */
	private final TreeMap<Integer, RESTData> BACKEND = new TreeMap<>();

	/**
	 * REST API for getting the total count of items
	 * <p>
	 * GET http://localhost:8080/count
	 */
	@GetMapping("/count")
	public int count() {
		return SIZE;
	}

	/**
	 * REST API for getting the items themselves
	 * <p>
	 * GET http://localhost:8080/data?count=50&offset=400
	 *
	 * @param count  how many items should be returned
	 * @param offset from what index the data should start
	 */
	@GetMapping("/data")
	public List<RESTData> data(int count, int offset) {

		System.out.println("Backend providing items " + offset + " to " + (offset + count));
		if (count > MAX_COUNT || count + offset > SIZE) {
			throw new RuntimeException("indexes outside bounds");
		}

		// make sure data is generated
		ensureData(count, offset);

		// fetch subset from the map
		final List<RESTData> list = new ArrayList<>(BACKEND.subMap(offset, offset + count).values());

		return list;
	}

	private void ensureData(int count, int offset) {

		for (int i = 0; i < count; i++) {

			final int dataIndex = offset + i;
			if (!BACKEND.containsKey(dataIndex)) {
				BACKEND.put(dataIndex, generateDataItem(dataIndex));
			}
		}
	}

	private RESTData generateDataItem(int dataIndex) {
		final RESTData data = new RESTData();
		data.setId(dataIndex);
		data.setTitle(generateString(5).replace(".", "").replace(",", ""));
		data.setMessage(generateString(15).replace(".", "").replace(",", ""));
		return data;
	}

	@SuppressWarnings("deprecation")
	private String generateString(int numWords) {
		final StringBuilder sb = new StringBuilder();

		for (int i = 0; i < numWords; i++) {
			final String word = words[rand.nextInt(words.length)];
			sb.append(word);
			sb.append(" ");
		}

		return WordUtils.capitalizeFully(sb.toString().trim()) + ".";
	}

}
