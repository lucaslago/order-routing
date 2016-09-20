package algorithm;


import model.InventoryItem;
import model.Map.RequestListMap;
import model.OrderItem;
import model.Warehouse;
import model.dto.Request;
import model.dto.Response;
import model.filter.FilterShippingMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderAlgorithm {
    private FilterShippingMethod filterShippingMethod = new FilterShippingMethod();
    private RequestListMap requestMap = new RequestListMap();



    public Response execute(Request request){

        Map<String, Integer> requestListMap = requestMap.getRequestList(request);


        Map<String, Integer> capacityMap = request.getWarehouseList()
                .stream()
                .collect(Collectors.toMap(Warehouse::getWarehouseName, Warehouse::getCapacity));

        List<InventoryItem> shipping = new ArrayList<>();

        List<InventoryItem> inventoryItemListFiltred = filterShippingMethod.getInventoryShippingMethodRequest(
                request.getInventoryItems(),
                request.getShippingMethodMethod(),
                request.getWarehouseList());

        for (OrderItem item : request.getOrderItemsList()) {

            for (InventoryItem inventory : inventoryItemListFiltred) {

                if (isSameProductNameAndQuantityNeededIsMoreThanZero(requestListMap, item, inventory)) {

                    int minValue = Math.min(requestListMap.get(item.getProductName()), inventory.getQuantityAvailable() );
                    int neededQuantity = requestListMap.get(item.getProductName()) - inventory.getQuantityAvailable();

                    insertNewValueInRequestMap(requestListMap, item, Math.max(0, neededQuantity));
                    shipping.add(new InventoryItem(inventory.getWarehouseName(), inventory.getProductName(), minValue));
                }
            }
        }
        return new Response(shipping);
    }

    private void insertNewValueInRequestMap(Map<String, Integer> requestListMap, OrderItem item, int value) {
        requestListMap.put(item.getProductName(), value);
    }

    private boolean isSameProductNameAndQuantityNeededIsMoreThanZero(Map<String, Integer> requestListMap, OrderItem item, InventoryItem inventory) {
        return inventory.getProductName().equals(item.getProductName())
                && (requestListMap.get(item.getProductName()) > 0);
    }

}
