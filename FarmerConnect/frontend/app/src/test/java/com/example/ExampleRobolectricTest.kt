package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.FarmerRepository
import com.example.localization.AppStrings
import com.example.localization.Language
import com.example.model.ProductCategory
import com.example.model.UserRole
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("FarmerConnect", appName)
    }

    @Test
    fun `verify bilingual localization`() {
        val englishTitle = AppStrings.appTitle.get(Language.ENGLISH)
        val kannadaTitle = AppStrings.appTitle.get(Language.KANNADA)
        assertEquals("FarmerConnect", englishTitle)
        assertEquals("ಫಾರ್ಮರ್ ಕನೆಕ್ಟ್", kannadaTitle)
    }

    @Test
    fun `verify repository initial products and mandi prices`() {
        val repo = FarmerRepository()
        val products = repo.products.value
        val mandiPrices = repo.mandiPrices.value

        assertTrue(products.isNotEmpty())
        assertTrue(mandiPrices.isNotEmpty())
        assertEquals("PROD-01", products.first().id)
        assertTrue(mandiPrices.first().history.isNotEmpty())
    }

    @Test
    fun `verify place order and UPI payment workflow`() {
        val repo = FarmerRepository()
        val product = repo.products.value.first()
        val initialOrdersCount = repo.orders.value.size

        val placedOrder = repo.placeOrder(product, 2.0, "Mandi Yard, Mandya")

        val newOrders = repo.orders.value
        assertEquals(initialOrdersCount + 1, newOrders.size)
        assertEquals(product.id, newOrders.first().productId)
        assertEquals("Pending", placedOrder.paymentStatus)

        // Mark paid via UPI
        repo.markOrderAsPaid(placedOrder.id, "UPI/TEST/123456")
        val updatedOrder = repo.orders.value.find { it.id == placedOrder.id }
        assertNotNull(updatedOrder)
        assertEquals("Paid via UPI", updatedOrder?.paymentStatus)
    }

    @Test
    fun `verify fertilizer recommendation calculator`() {
        val repo = FarmerRepository()
        val rec = repo.calculateFertilizer("Paddy", 2.0)
        assertTrue(rec.ureaBags >= 2)
        assertTrue(rec.dapBags >= 1)
        assertTrue(rec.estimatedCost > 0)
    }

    @Test
    fun `verify labor booking workflow`() {
        val repo = FarmerRepository()
        val labor = repo.laborListings.value.first()
        repo.bookLabor(labor, "Harvesting", 4, "Tomorrow")

        val bookings = repo.laborBookings.value
        assertTrue(bookings.isNotEmpty())
        assertEquals(4, bookings.first().workersCount)
    }

    @Test
    fun `verify product reviews`() {
        val repo = FarmerRepository()
        val product = repo.products.value.first()
        val initialReviews = product.reviews.size

        repo.addProductReview(product.id, "Test Trader", 5, "Super grain quality")
        val updatedProduct = repo.products.value.find { it.id == product.id }
        assertNotNull(updatedProduct)
        assertEquals(initialReviews + 1, updatedProduct?.reviews?.size)
    }
}
