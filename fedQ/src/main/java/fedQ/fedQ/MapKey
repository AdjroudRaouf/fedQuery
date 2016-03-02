/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fedQuery;

/**
 *
 * @author adjroud
 */

        public class MapKey {

	public MapKey() {

	}

	public MapKey(Integer source, Integer request) {

		this.request = request;
		this.source = source;
	}

	private Integer request;
	private Integer source;

	public int getRequest() {
		return request;
	}

	public void setRequest(Integer request) {
		this.request = request;
	}

	public int getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((request == null) ? 0 : request.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MapKey other = (MapKey) obj;
		if (request == null) {
			if (other.request != null)
				return false;
		} else if (!request.equals(other.request))
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		return true;
	}


}
