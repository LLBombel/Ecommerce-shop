package com.rafalropel.ecommerceshop.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.rafalropel.ecommerceshop.*
import com.rafalropel.ecommerceshop.model.*
import com.rafalropel.ecommerceshop.ui.dashboard.ProductsFragment
import com.rafalropel.ecommerceshop.ui.home.HomeFragment
import com.rafalropel.ecommerceshop.ui.notifications.OrdersFragment
import com.rafalropel.ecommerceshop.utils.Constants

class FireStoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User) {
        mFireStore.collection(Constants.USERS)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.registrationSuccess()
            }
            .addOnFailureListener {
                Log.e(activity.javaClass.simpleName, "Błąd")
            }
    }

    fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser

        var currentUserID = ""

        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    fun getUserDetails(activity: Activity) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())
                val user = document.toObject(User::class.java)!!

                val sharedPreferences = activity.getSharedPreferences(
                    Constants.SHOPPOLANDIA_PREFERENES,
                    Context.MODE_PRIVATE
                )

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(
                    Constants.LOGGED_IN_USERNAME, user.nameSurname
                )
                editor.apply()

                when (activity) {
                    is LoginActivity -> {
                        activity.logInSuccess(user)
                    }

                    is SettingsActivity -> {
                        activity.userDetailsSuccess(user)

                    }
                }
            }
    }

    fun updateUser(activity: Activity, userHashMap: HashMap<String, Any>) {
        mFireStore.collection(Constants.USERS).document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {
                when (activity) {
                    is UserProfileActivity -> {
                        activity.userProfileUpdateSuccess()
                    }
                }

            }
            .addOnFailureListener { e ->
                when (activity) {
                    is UserProfileActivity -> {
                        Log.e(activity.javaClass.simpleName, "Błąd", e)
                    }
                }
            }

    }

    fun uploadImage(activity: Activity, imageFileURI: Uri?, imageType: String) {


        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            imageType + System.currentTimeMillis() + "."
                    + Constants.getFileExtension(
                activity,
                imageFileURI
            )
        )


        sRef.putFile(imageFileURI!!)
            .addOnSuccessListener { taskSnapshot ->

                Log.e(
                    "Firebase Image URL",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()

                )


                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        Log.e("Downloadable Image URL", uri.toString())
                        when (activity) {
                            is UserProfileActivity -> {
                                activity.imageUploadSuccess(uri.toString())
                            }

                            is AddProductActivity -> {
                                activity.imageUploadSuccess(uri.toString())

                            }
                        }

                    }
            }
            .addOnFailureListener { exception ->


                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }
    }

    fun uploadProduct(activity: AddProductActivity, productInfo: Product) {
        mFireStore.collection(Constants.PRODUCTS)
            .document()
            .set(productInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.productUploadSuccess()
            }
            .addOnFailureListener {
                Log.e(activity.javaClass.simpleName, "Błąd")
            }
    }

    fun getProductsList(fragment: Fragment) {
        mFireStore.collection(Constants.PRODUCTS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.e("Products", document.documents.toString())

                val productsList: ArrayList<Product> = ArrayList()
                for (i in document.documents) {
                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id

                    productsList.add(product)


                }
                when (fragment) {
                    is ProductsFragment -> {
                        fragment.getProductsListSuccess(productsList)
                    }
                }

            }
    }


    fun getHomeItemsList(fragment: HomeFragment) {
        mFireStore.collection(Constants.PRODUCTS)
            .get()
            .addOnSuccessListener { document ->
                Log.e(fragment.javaClass.simpleName, document.documents.toString())

                val productsList: ArrayList<Product> = ArrayList()

                for (i in document.documents) {
                    val product = i.toObject(Product::class.java)!!
                    product.product_id = i.id
                    productsList.add(product)
                }

                when (fragment) {
                    is HomeFragment -> {
                        fragment.getHomeItemsSuccess(productsList)
                    }
                }

            }
            .addOnFailureListener {
                Log.e(fragment.javaClass.simpleName, "Błąd")
            }
    }

    fun deleteProduct(fragment: ProductsFragment, productID: String) {
        mFireStore.collection(Constants.PRODUCTS)
            .document(productID)
            .delete()
            .addOnSuccessListener {
                fragment.deleteProductSuccess()
            }
            .addOnFailureListener {
                Log.e(fragment.requireActivity().javaClass.simpleName, "Błąd")
            }
    }

    fun getProductDetails(activity: ProductDetailsActivity, productId: String) {
        mFireStore.collection(Constants.PRODUCTS)
            .document(productId)
            .get()
            .addOnSuccessListener { document ->
                val product = document.toObject(Product::class.java)
                if (product != null) {
                    activity.productDetailsSuccess(product)
                }

            }
            .addOnFailureListener {
                Log.e(activity.javaClass.simpleName, "Błąd")
            }
    }

    fun addCartItems(activity: ProductDetailsActivity, addToCart: Cart) {
        mFireStore.collection(Constants.CART_ITEMS)
            .document()
            .set(addToCart, SetOptions.merge())
            .addOnSuccessListener {
                activity.addToCartSuccess()
            }
            .addOnFailureListener {
                Log.e(activity.javaClass.simpleName, "Błąd")
            }
    }

    fun checkIfItemExistInCart(activity: ProductDetailsActivity, productId: String) {
        mFireStore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .whereEqualTo(Constants.PRODUCT_ID, productId)
            .get()
            .addOnSuccessListener { document ->
                if (document.documents.size > 0) {
                    activity.productExistInCart()
                }

            }
            .addOnFailureListener {
                Log.e(activity.javaClass.simpleName, "Błąd")
            }
    }

    fun getCartList(activity: Activity) {
        mFireStore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                val list: ArrayList<Cart> = ArrayList()

                for (i in document.documents) {
                    val cartItem = i.toObject(Cart::class.java)
                    cartItem!!.id = i.id
                    list.add(cartItem)
                }
                when (activity) {
                    is CartListActivity -> {
                        activity.successCartItemsList(list)
                    }
                    is CheckoutActivity -> {
                        activity.successCartItemsList(list)
                    }

                }
            }
            .addOnFailureListener {
                Log.e(activity.javaClass.simpleName, "Błąd")
            }
    }

    fun getAllProductsList(activity: Activity) {
        mFireStore.collection(Constants.PRODUCTS)
            .get()
            .addOnSuccessListener { document ->
                val productsList: ArrayList<Product> = ArrayList()
                for (i in document.documents) {
                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id

                    productsList.add(product)
                }

                when (activity) {
                    is CartListActivity -> {
                        activity.successProductsListFromFirestore(productsList)
                    }
                    is CheckoutActivity -> {
                        activity.successProductsListFromFirestore(productsList)
                    }
                }

            }
            .addOnFailureListener {
                Log.e("product list", "Błąd")
            }
    }

    fun removeItemFromCart(context: Context, cart_id: String) {
        mFireStore.collection(Constants.CART_ITEMS)
            .document(cart_id)
            .delete()
            .addOnSuccessListener {
                when (context) {
                    is CartListActivity -> {
                        context.itemRemoveSuccess()
                    }
                }

            }
            .addOnFailureListener {
                Log.e(context.javaClass.simpleName, "Błąd")
            }
    }

    fun updateCart(context: Context, cart_id: String, itemHashMap: HashMap<String, Any>) {
        mFireStore.collection(Constants.CART_ITEMS)
            .document(cart_id)
            .update(itemHashMap)
            .addOnSuccessListener {
                when (context) {
                    is CartListActivity -> {
                        context.updateCartSuccess()
                    }
                }
            }
            .addOnFailureListener {
                Log.e(context.javaClass.simpleName, "Błąd")
            }
    }

    fun addAddress(activity: AddEditAddressActivity, addressInfo: Address) {
        mFireStore.collection(Constants.ADDRESSES)
            .document()
            .set(addressInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.addAddressSuccess()
            }
            .addOnFailureListener {
                Log.e(activity.javaClass.simpleName, "Błąd")
            }
    }

    fun getAddressesList(activity: AddressListActivity) {
        mFireStore.collection(Constants.ADDRESSES)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.e(activity.javaClass.simpleName, document.documents.toString())

                val addressList: ArrayList<Address> = ArrayList()

                for (i in document.documents) {
                    val address = i.toObject(Address::class.java)!!

                    address.id = i.id
                    addressList.add(address)
                }
                activity.successAddressListFromFirestore(addressList)
            }
            .addOnFailureListener {
                Log.e(activity.javaClass.simpleName, "Błąd")
            }
    }

    fun updateAddress(activity: AddEditAddressActivity, addressInfo: Address, addressId: String) {
        mFireStore.collection(Constants.ADDRESSES)
            .document(addressId)
            .set(addressInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.addAddressSuccess()
            }
            .addOnFailureListener {
                Log.e(activity.javaClass.simpleName, "Błąd")
            }
    }

    fun deleteAddress(activity: AddressListActivity, addressId: String) {
        mFireStore.collection(Constants.ADDRESSES)
            .document(addressId)
            .delete()
            .addOnSuccessListener {
                activity.deleteAddressSuccess()
            }
            .addOnFailureListener {
                Log.e(activity.javaClass.simpleName, "Błąd")
            }
    }

    fun placeOrder(activity: CheckoutActivity, order: Order) {
        mFireStore.collection(Constants.ORDERS)
            .document()
            .set(order, SetOptions.merge())
            .addOnSuccessListener {
                activity.placeOrderSuccess()
            }
            .addOnFailureListener {
                Log.e(activity.javaClass.simpleName, "Błąd")
            }
    }

    fun updateAllDetails(activity: CheckoutActivity, cartList: ArrayList<Cart>, order: Order) {
        val writeBatch = mFireStore.batch()
        for (cartItem in cartList) {
            val soldProduct = SoldProduct(
                cartItem.product_owner_id,
                cartItem.title,
                cartItem.price,
                cartItem.cart_amount,
                cartItem.image,
                order.title,
                order.order_date,
                order.subtotal_amount,
                order.shipping_charge,
                order.total_amount,
                order.address
            )

            val documentReference = mFireStore.collection(Constants.SOLD_PRODUCTS)
                .document(cartItem.product_id)

            writeBatch.set(documentReference, soldProduct)
        }

        for (cartItem in cartList) {
            val documentReference = mFireStore.collection(Constants.CART_ITEMS)
                .document(cartItem.id)
            writeBatch.delete(documentReference)
        }

        writeBatch.commit().addOnSuccessListener {
            activity.detailsUpdateSuccess()
        }.addOnFailureListener {
            Log.e(activity.javaClass.simpleName, "Błąd")
        }

    }

    fun getOrdersList(fragment: OrdersFragment) {
        mFireStore.collection(Constants.ORDERS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                val list: ArrayList<Order> = ArrayList()

                for (i in document.documents) {
                    val orderItem = i.toObject(Order::class.java)!!
                    orderItem.id = i.id
                    list.add(orderItem)
                }
                fragment.populateOrdersList(list)
            }
            .addOnFailureListener {
                Log.e(fragment.javaClass.simpleName, "Błąd")
            }
    }

    fun getSoldProducts(fragment: SoldProductsFragment){
        mFireStore.collection(Constants.SOLD_PRODUCTS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                val list: ArrayList<SoldProduct> = ArrayList()
                for (i in document.documents){
                    val soldProduct = i.toObject(SoldProduct::class.java)!!
                    soldProduct.id = i.id

                    list.add(soldProduct)
                }
                fragment.successSoldProducts(list)
            }
            .addOnFailureListener {
                Log.e(fragment.javaClass.simpleName, "Błąd")
            }
    }
}