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

let Url = 'http://localhost:8080';

function purchase() {
    fetch(`${Url}/api/cart/purchase`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(data => {
        console.log("Purchase successful", data);
        // After a successful purchase, re-render the cart to reflect the cleared items
        funcartItems();
    })
    .catch(error => console.error('Error during purchase:', error));
}

function deleteCartItem(bookId) {
    fetch(`${Url}/api/cart/remove/${bookId}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (response.ok) {
            console.log(`Book with ID ${bookId} deleted successfully`);
            // Remove the row directly from the DOM after deletion
            const row = document.querySelector(`tr[data-cartitem-id="${bookId}"]`);
            if (row) {
                row.remove();
            }
            // Recalculate totals after deletion
            calculateTotals();
        } else {
            console.error(`Error deleting book with ID ${bookId}`);
        }
    })
    .catch(error => console.error('Error deleting cart item:', error));
}

function funcartItems() {
    fetch(`${Url}/api/cart`)
    .then(response => response.json())
    .then(data => {
        console.log('Cart data:', data);
        const cartsTbody = document.getElementById('carts-tbody');
        
        // Clear the tbody before re-rendering
        cartsTbody.innerHTML = '';

        // Check if cartItems exist
        if (data.cartItems && data.cartItems.length > 0) {
            // Dynamically generate rows for each cart item
            data.cartItems.forEach(cart => {
                console.log('Cart item:', cart);
                const row = document.createElement('tr');
                let { book, id, quantity, subTotal, unitPrice } = cart;
                
                // Create a row for the book
                row.setAttribute('data-cartitem-id', book.id);  // Assign a data attribute for easier deletion
                row.innerHTML = `
                    <td>${book.title}</td>
                    <td>${unitPrice}</td>
                    <td>${quantity}</td>
                    <td>${subTotal}</td>
                    <td>
                        <button class="delete-cart-btn" data-cartitem-id="${book.id}">Delete</button>
                    </td>
                `;
                
                // Append the row to the tbody
                cartsTbody.appendChild(row);

                // Add the delete event listener for each button
                const deleteCartItemBtn = row.querySelector('.delete-cart-btn');
                deleteCartItemBtn.addEventListener('click', () => {
                    deleteCartItem(book.id);
                });
            });
        } else {
            console.log('No items in the cart.');
        }
        // Calculate totals after rendering cart items
        calculateTotals();
    })
    .catch(error => console.error('Error fetching cart items:', error));
}

function calculateTotals() {
    fetch(`${Url}/api/cart`)
    .then(response => response.json())
    .then(data => {
        const total = data.total || 0;
        const cgst = data.cgst || 0;
        const sgst = data.sgst || 0;
        const grandTotal = data.grandTotal || 0;

        document.getElementById('cart-total').innerText = total.toFixed(2);
        document.getElementById('cart-cgst').innerText = cgst.toFixed(2);
        document.getElementById('cart-sgst').innerText = sgst.toFixed(2);
        document.getElementById('cart-grand-total').innerText = grandTotal.toFixed(2);
    })
    .catch(error => console.error('Error calculating totals:', error));
}

// Initial load of cart items
funcartItems();