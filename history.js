window.onload = function() {
    const currentPage = window.location.pathname.split("/").pop(); // Get the current page name
    const navLinks = document.querySelectorAll("nav ul li a");

    navLinks.forEach(link => {
        if (link.getAttribute("href") === currentPage) {
            link.classList.add("active"); // Add active class to the current link
        } else {
            link.classList.remove("active"); // Remove active class from other links
        }
    });
};

document.addEventListener('DOMContentLoaded', () => {
    const orderHistoryContainer = document.getElementById('order-history');

    // Fetch order history from API
    fetch('http://localhost:8080/api/cart/history')
        .then(response => response.json())
        .then(orders => {
            if (orders.length === 0) {
                orderHistoryContainer.innerHTML = '<p class="no-orders">No orders found.</p>';
            } else {
                orders.forEach(order => {
                    const orderElement = document.createElement('div');
                    orderElement.classList.add('order');

                    const orderHeader = document.createElement('div');
                    orderHeader.classList.add('order-header');
                    orderHeader.innerHTML = `
                        <span>Order ID: ${order.id}</span>
                        <span>Order Date: ${new Date(order.orderDate).toLocaleString()}</span>
                    `;
                    orderElement.appendChild(orderHeader);

                    const orderDetails = document.createElement('div');
                    orderDetails.classList.add('order-details');

                    if (order.orderedItems.length === 0) {
                        orderDetails.innerHTML = '<p>No items in this order.</p>';
                    } else {
                        order.orderedItems.forEach(item => {
                            const orderItem = document.createElement('div');
                            orderItem.classList.add('order-item');
                            orderItem.innerHTML = `
                                <span>${item.book.title} (x${item.quantity})</span>
                                <span>₹${item.subTotal.toFixed(2)}</span>
                            `;
                            orderDetails.appendChild(orderItem);
                        });
                    }

                    const orderTotal = document.createElement('div');
                    orderTotal.classList.add('order-total');
                    orderTotal.innerHTML = `
                        <p>Total: ₹${order.total.toFixed(2)}</p>
                        <p>CGST: ₹${order.cgst.toFixed(2)}</p>
                        <p>SGST: ₹${order.sgst.toFixed(2)}</p>
                        <p>Grand Total: ₹${order.grandTotal.toFixed(2)}</p>
                    `;
                    orderDetails.appendChild(orderTotal);

                    orderElement.appendChild(orderDetails);
                    orderHistoryContainer.appendChild(orderElement);
                });
            }
        })
        .catch(error => {
            console.error('Error fetching order history:', error);
            orderHistoryContainer.innerHTML = '<p class="no-orders">Failed to load order history. Please try again later.</p>';
        });
});