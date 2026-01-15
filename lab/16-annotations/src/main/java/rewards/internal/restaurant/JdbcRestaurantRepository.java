package rewards.internal.restaurant;

import common.money.Percentage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Loads restaurants from a data source using the JDBC API.
 *
 * This implementation should cache restaurants to improve performance. The
 * cache should be populated on initialization and cleared on destruction.
 */
@Repository
public class JdbcRestaurantRepository implements RestaurantRepository {

	private DataSource dataSource;

	/**
	 * The Restaurant object cache. Cached restaurants are indexed
	 * by their merchant numbers.
	 */
	private Map<String, Restaurant> restaurantCache;

	/**
	 * The constructor sets the data source this repository will use to load
	 * restaurants. When the instance of JdbcRestaurantRepository is created, a
	 * Restaurant cache is populated for read only access
	 */
	public JdbcRestaurantRepository(DataSource dataSource) {
		this.dataSource = dataSource;
		// Used to populate the restaurant cache in the constructor - a map of restaurants with the key being the merchant number.
		// The constructor is meant to only initialize the object so moved the populating of the cache into a method annotated
		// with @PostConstruct.

	}

	public JdbcRestaurantRepository() {
		// no-args constructor called by Spring as @Autowired not used on any constructor.
	}

	// Inject the Datasource dependency using setter based injection.
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Restaurant findByMerchantNumber(String merchantNumber) {
		return queryRestaurantCache(merchantNumber);
	}

	/**
	 * Helper method that populates the restaurantCache restaurant object
	 * caches from the rows in the T_RESTAURANT table. Cached restaurants are indexed
	 * by their merchant numbers. This method should be called on initialization.
	 */

	// Call the method below after initializing this bean and making available the dependencies required by it.
	// @PostConstruct and @PreDestroy was introduced in JDK 6.
	@PostConstruct
	void populateRestaurantCache() {
		restaurantCache = new HashMap<String, Restaurant>();
		String sql = "select MERCHANT_NUMBER, NAME, BENEFIT_PERCENTAGE from T_RESTAURANT";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				Restaurant restaurant = mapRestaurant(rs);
				// index the restaurant by its merchant number
				restaurantCache.put(restaurant.getNumber(), restaurant);
			}
		} catch (SQLException e) {
			throw new RuntimeException("SQL exception occurred finding by merchant number", e);
		} finally {
			if (rs != null) {
				try {
					// Close to prevent database cursor exhaustion
					rs.close();
				} catch (SQLException ex) {
				}
			}
			if (ps != null) {
				try {
					// Close to prevent database cursor exhaustion
					ps.close();
				} catch (SQLException ex) {
				}
			}
			if (conn != null) {
				try {
					// Close to prevent database connection exhaustion
					conn.close();
				} catch (SQLException ex) {
				}
			}
		}
	}

	/**
	 * Helper method that simply queries the cache of restaurants.
	 *
	 * @param merchantNumber
	 *            the restaurant's merchant number
	 * @return the restaurant
	 * @throws EmptyResultDataAccessException
	 *             if no restaurant was found with that merchant number
	 */
	private Restaurant queryRestaurantCache(String merchantNumber) {
		Restaurant restaurant = restaurantCache.get(merchantNumber);
		if (restaurant == null) {
			throw new EmptyResultDataAccessException(1);
		}
		return restaurant;
	}

	/**
	 * Helper method that clears the cache of restaurants.
	 * This method should be called when a bean is destroyed.
	 *
	 * TODO-10: Add a scheme to check if this method is being invoked
	 * - Add System.out.println to this method.
	 *
	 * TODO-11: Have this method to be invoked before a bean gets destroyed
	 * - Re-run RewardNetworkTests.
	 * - Observe this method is not called.
	 * - Use an appropriate annotation to register this method for a
	 *   destruction lifecycle callback.
	 * - Re-run the test and you should be able to see
	 *   that this method is now being called.
	 */
	// Use the @PreDestroy annotation added in Java 6 to call the method below before destroying the bean.
	@PreDestroy
	public void clearRestaurantCache() {
		System.out.println("clearResturantCache() invoked.");
		restaurantCache.clear();
	}

	/**
	 * Maps a row returned from a query of T_RESTAURANT to a Restaurant object.
	 *
	 * @param rs
	 *            the result set with its cursor positioned at the current row
	 */
	private Restaurant mapRestaurant(ResultSet rs) throws SQLException {
		// get the row column data
		String name = rs.getString("NAME");
		String number = rs.getString("MERCHANT_NUMBER");
		Percentage benefitPercentage = Percentage.valueOf(rs.getString("BENEFIT_PERCENTAGE"));
		// map to the object
		Restaurant restaurant = new Restaurant(number, name);
		restaurant.setBenefitPercentage(benefitPercentage);
		return restaurant;
	}
}