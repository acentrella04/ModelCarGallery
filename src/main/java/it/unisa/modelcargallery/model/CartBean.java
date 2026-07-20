package it.unisa.modelcargallery.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<ProductBean> products;

    public CartBean() {
        products = new ArrayList<ProductBean>();
    }

    /*
     * Aggiunge una copia del prodotto.
     * Se il prodotto è già presente, aumenta di fatto la quantità.
     */
    public void addProduct(ProductBean product) {
        products.add(product);
    }

    /*
     * Elimina una sola copia del prodotto.
     * Corrisponde alla diminuzione della quantità di uno.
     */
    public void deleteOneProduct(int code) {

        for (int i = 0; i < products.size(); i++) {

            ProductBean product = products.get(i);

            if (product.getCode() == code) {
                products.remove(i);
                return;
            }
        }
    }

    /*
     * Elimina tutte le copie dello stesso prodotto.
     */
    public void deleteAllProducts(int code) {

        for (int i = products.size() - 1; i >= 0; i--) {

            ProductBean product = products.get(i);

            if (product.getCode() == code) {
                products.remove(i);
            }
        }
    }

    /*
     * Svuota completamente il carrello.
     */
    public void clearCart() {
        products.clear();
    }

    /*
     * Restituisce la quantità di un prodotto nel carrello.
     */
    public int getQuantity(int code) {

        int quantity = 0;

        for (ProductBean product : products) {

            if (product.getCode() == code) {
                quantity++;
            }
        }

        return quantity;
    }

    /*
     * Restituisce una lista contenente ogni prodotto
     * una sola volta.
     */
    public List<ProductBean> getDistinctProducts() {

        List<ProductBean> distinctProducts =
                new ArrayList<ProductBean>();

        for (ProductBean product : products) {

            boolean found = false;

            for (ProductBean distinctProduct : distinctProducts) {

                if (distinctProduct.getCode() ==
                        product.getCode()) {

                    found = true;
                    break;
                }
            }

            if (!found) {
                distinctProducts.add(product);
            }
        }

        return distinctProducts;
    }

    /*
     * Calcola il totale del carrello.
     */
    public float getTotal() {

        float total = 0;

        for (ProductBean product : products) {
            total += product.getPrice();
        }

        return total;
    }

    public List<ProductBean> getProducts() {
        return products;
    }
}