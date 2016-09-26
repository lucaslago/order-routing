package model.filter;

import model.InventoryItem;
import model.ShippingMethod;
import model.Warehouse;
import model.dto.Request;
import org.junit.Before;
import org.junit.Test;
import strategy.NoneInventoryStrategy;
import java.util.List;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class FilterShippingMethodTest {
    private FilterShippingMethod filterShippingMethod;
    private InventoryItem inventoryItem;
    private Warehouse brazilWarehouse;
    private Warehouse chileWarehouse;

    @Before
    public void setUp() throws Exception {
        this.filterShippingMethod = new FilterShippingMethod();

        this.inventoryItem = new InventoryItem("BRAZIL", "Mouse", 2);
        this.brazilWarehouse = new Warehouse("BRAZIL", asList(ShippingMethod.UPS, ShippingMethod.FEDEX), 10);
        this.chileWarehouse = new Warehouse("Chile", asList(ShippingMethod.FEDEX, ShippingMethod.DHL), 10);
    }

    @Test
    public void shouldReturnBrazilInventoryItemWhenUpsShippingMethodIsPassed() throws Exception {

        Request request = new Request(asList(
                new InventoryItem("BRAZIL", "Mouse", 2)),
                asList(brazilWarehouse, chileWarehouse),
                ShippingMethod.UPS,
                asList( null, null),
                new NoneInventoryStrategy());

        List<InventoryItem> response = filterShippingMethod.getInventoryListFiltredByShippingMethodRequest(request);
        List<InventoryItem> actual = asList(inventoryItem);

        assertThat(actual, is(response));
     }
}