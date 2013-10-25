package gr.bytecode.apps.android.phonebook.services;

import gr.bytecode.apps.android.phonebook.entities.Entry;
import gr.bytecode.apps.android.phonebook.entities.EntryCategory;
import gr.bytecode.apps.android.phonebook.entities.Version;
import gr.bytecode.apps.android.phonebook.exceptions.DataMissingException;
import gr.bytecode.apps.android.phonebook.exceptions.InvalidDataException;

import java.io.IOException;
import java.util.Iterator;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A Parser of Phonebook data, formed in JavaScript Object Notation (JSON)
 * 
 * @author Dimitris Balaouras
 * @copyright 2013 ByteCode.gr
 * 
 */
public class JsonDataParser implements DataParser<String> {

	// Default Jackson Json ObjectMapper
	private ObjectMapper mapper = new ObjectMapper();

	/**
	 * a JsonNode instance that holds the root of the data tree
	 */
	private JsonNode rootNode;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gr.bytecode.apps.android.phonebook.services.DataParser#setData(java.lang
	 * .Object)
	 */
	@Override
	public void setData(String data) {

		// create a parser for the given data
		try {

			// create a Jackson json parser
			JsonParser jp = mapper.getJsonFactory().createJsonParser(data);

			// locate and set the rootNode
			rootNode = mapper.readTree(jp);

		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gr.bytecode.apps.android.phonebook.services.DataParser#getEntriesIterator
	 * ()
	 */
	@Override
	public Iterator<Entry> getEntriesIterator() {

		return new EntityIterator<Entry>(locateNode("entries").iterator(), new EntriesMapper());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gr.bytecode.apps.android.phonebook.services.DataParser#
	 * getEntryCategoryIterator()
	 */
	@Override
	public Iterator<EntryCategory> getEntryCategoryIterator() {

		return new EntityIterator<EntryCategory>(locateNode("categories").iterator(),
				new EntryCategoryMapper());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gr.bytecode.apps.android.phonebook.services.DataParser#getVersionIterator
	 * ()
	 */
	@Override
	public Iterator<Version> getVersionIterator() {

		return new EntityIterator<Version>(locateNode("versions").iterator(), new VersionMapper());
	}

	/**
	 * Mapper of Entries.
	 */
	private class EntriesMapper implements EntityDataMapper<Entry> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see gr.bytecode.apps.android.phonebook.services.JsonDataParser.
		 * IEntityDataMapper#map(com.fasterxml.jackson.databind.JsonNode,
		 * com.fasterxml.jackson.databind.ObjectMapper)
		 */
		public Entry map(JsonNode node, ObjectMapper mapper) throws JsonParseException,
				JsonMappingException, IOException {

			// parse latitude
			Float latitude = Float.valueOf(0);
			String lat = node.path("latitude").asText();
			if ("".equals(lat) == false) {
				latitude = Float.parseFloat(lat);
			}

			// parse longitude
			Float longitude = Float.valueOf(0);
			String lon = node.path("longtitude").asText();
			if ("".equals(lon) == false) {
				longitude = Float.parseFloat(lon);
			}

			Entry entry = new Entry(node.path("name").asText(), node.path("telephoneNumber")
					.asText(), node.path("categoryId").asInt(),
					node.path("streetAddress").asText(), node.path("website").asText(), node.path(
							"emailAddress").asText(), latitude, longitude);

			// set the external category Id
			entry.setExternalCategoryId(node.path("categoryId").asInt());

			return entry;

		}
	}

	/**
	 * Mapper of EntryCategories.
	 */
	private class EntryCategoryMapper implements EntityDataMapper<EntryCategory> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see gr.bytecode.apps.android.phonebook.services.JsonDataParser.
		 * EntityDataMapper#map(com.fasterxml.jackson.databind.JsonNode,
		 * com.fasterxml.jackson.databind.ObjectMapper)
		 */
		public EntryCategory map(JsonNode node, ObjectMapper mapper) throws JsonParseException,
				JsonMappingException, IOException {

			EntryCategory entryCategory = new EntryCategory(node.path("id").asInt(), node.path(
					"name").asText(), node.path("description").asText());

			return entryCategory;

		}
	}

	/**
	 * Mapper of Versions.
	 */
	private class VersionMapper implements EntityDataMapper<Version> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see gr.bytecode.apps.android.phonebook.services.JsonDataParser.
		 * IEntityDataMapper#map(com.fasterxml.jackson.databind.JsonNode,
		 * com.fasterxml.jackson.databind.ObjectMapper)
		 */
		public Version map(JsonNode node, ObjectMapper mapper) throws JsonParseException,
				JsonMappingException, IOException {

			Version version = new Version(node.path("Id").asInt(), node.path("Name").asText(), node
					.path("Version").asInt());

			return version;

		}
	}

	/**
	 * Locates a node within a parent node
	 * 
	 * @param nodeName
	 * @param rootNode
	 * @return
	 * @throws DataMissingException
	 * @throws InvalidDataException
	 * @throws IllegalArgumentException
	 */
	private JsonNode locateNode(String nodeName) {

		JsonNode itemsNode = rootNode.path(nodeName);
		return itemsNode;
	}

	/**
	 * @author Dimitris Balaouras
	 * @copyright 2013 ByteCode.gr
	 * 
	 * @param <E>
	 */
	private class EntityIterator<E> implements Iterator<E> {

		private EntityDataMapper<E> dataMapper;

		/**
		 * Our json iterator
		 */
		private Iterator<JsonNode> iterator;

		/**
		 * Default Constructor
		 * 
		 * @param jsonNode
		 * @param dataBinder
		 */
		public EntityIterator(Iterator<JsonNode> iterator, EntityDataMapper<E> dataMapper) {

			this.dataMapper = dataMapper;
			this.iterator = iterator;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext() {

			return iterator != null && iterator.hasNext();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Iterator#next()
		 */
		@Override
		public E next() {

			try {

				return dataMapper.map(iterator.next(), mapper);

			} catch (Exception e) {

				e.printStackTrace();
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove() {

			iterator.remove();

		}

	}

	/**
	 * DataMapper Interface (see Command pattern)
	 * 
	 * @param <E>
	 */
	private interface EntityDataMapper<E> {

		public E map(JsonNode node, ObjectMapper mapper) throws JsonParseException,
				JsonMappingException, IOException;
	}

}
