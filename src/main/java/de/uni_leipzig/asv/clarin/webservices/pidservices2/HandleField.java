package de.uni_leipzig.asv.clarin.webservices.pidservices2;

/**
 * Fields that can be stored via the EPIC v2 API. This enumeration is a
 * restriction of allowed fields <em>in this project</em>. The EPIC API does not
 * have such restrictions.
 * 
 * @author Thomas Eckart
 */
public enum HandleField {
	AUTHORS("authors", "AUTHORS"), CREATOR("creator", "CREATOR"), EXPDATE("expirationDate", "EXPDATE"), INST(
			"institution", "INST"), METADATA_URL("MetadataURL", "METADATA_URL"), PUBDATE("publicationDate",
					"PUBDATE"), TITLE("title", "21.T11148/4b18b74f5ed1441bc6a3"), URL("url", "URL");

	private String name;
	private String type;

	private HandleField(String name, String type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}
}
