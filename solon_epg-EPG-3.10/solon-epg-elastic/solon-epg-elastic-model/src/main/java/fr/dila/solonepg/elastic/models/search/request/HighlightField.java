package fr.dila.solonepg.elastic.models.search.request;

import java.io.Serializable;

public class HighlightField implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String FRAGMENT_SIZE = "fragment_size";
	public static final String NUMBER_OF_FRAGMENTS = "number_of_fragments";

	private String type;

	private int fragmentSize;

	private int numberOfFragments;

	public HighlightField(String type, int fragmentSize, int numberOfFragments) {
		this.type = type;
		this.fragmentSize = fragmentSize;
		this.numberOfFragments = numberOfFragments;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getFragmentSize() {
		return fragmentSize;
	}

	public void setFragmentSize(int fragmentSize) {
		this.fragmentSize = fragmentSize;
	}

	public int getNumberOfFragments() {
		return numberOfFragments;
	}

	public void setNumberOfFragments(int numberOfFragments) {
		this.numberOfFragments = numberOfFragments;
	}
}