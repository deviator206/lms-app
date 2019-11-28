package repository;

public class RepositoryHelper {
	public static String getPaginatedQuery(String query, Integer startPage, Integer pageSize) {
		boolean endsWithColon = query.endsWith(";");
		String tempQuery = null;
		if (endsWithColon) {
			tempQuery = query.substring(0, query.length() - 1);
		} else {
			tempQuery = query;
		}
		return tempQuery + " LIMIT " + pageSize + " OFFSET " + (startPage - 1) + " ;";
	}
}
