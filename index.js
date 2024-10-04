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

const addBookBtn = document.getElementById('add-book-btn');
const addBookForm = document.getElementById('add-book-form');
const addBookFormContent = document.getElementById('add-book-form-content');
const addBookSubmit = document.getElementById('add-book-submit');
const addBookCancel = document.getElementById('add-book-cancel');
const searchInput = document.getElementById('searchInput');
const searchButton = document.getElementById('searchButton');
const prevBtn = document.getElementById('prev-btn');
const nextBtn = document.getElementById('next-btn');
const currentPageSpan = document.getElementById('current-page');

let currentPage = 1;
const booksPerPage = 9;
let allBooks = [];

// Show the add book form
addBookBtn.addEventListener('click', () => {
    addBookForm.style.display = 'block';
});

// Hide the add book form
addBookCancel.addEventListener('click', () => {
    addBookForm.style.display = 'none';
});

// Add a book and refresh the books list
addBookSubmit.addEventListener('click', (e) => {
    e.preventDefault();
    const title = document.getElementById('title').value;
    const author = document.getElementById('author').value;
    const genre = document.getElementById('genre').value;
    const price = document.getElementById('price').value;
    const stockQuantity = document.getElementById('stock').value;
    const published = document.getElementById('Publishdate').value;
    console.log(title, author, genre, price, stockQuantity, published);
    fetch(`${Url}/books`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            title,
            price,
            stock: stockQuantity,
            published,
            authors: [{ authorName: author }],
            genres: [{ genreName: genre }]
        })
    })
    .then(response => response.json())
    .then(data => {
        console.log('Book added:', data);
        // Refresh the book list dynamically after adding a new book
        displayBooks();
        addBookForm.style.display = 'none'; // Close the form
        addBookFormContent.reset(); // Reset form fields
    })
    .catch(error => console.error('Error adding book:', error));
});

// Event listener for search functionality
searchButton.addEventListener('click', () => {
    const searchTerm = searchInput.value.toLowerCase();
    displayBooks(searchTerm);  // Pass the search term to displayBooks
});

// Fetch and display the books list with optional search term
// Fetch and display the books list with optional search term
function displayBooks(searchTerm = '') {
    fetch(`${Url}/books`)
    .then(response => response.json())
    .then(books => {
        allBooks = books;
        const filteredBooks = allBooks.filter(book => {
            const titleMatch = book.title.toLowerCase().includes(searchTerm);
            const authorMatch = book.authors && book.authors.some(author => author.authorName && author.authorName.toLowerCase().includes(searchTerm));
            const genreMatch = book.genres && book.genres.some(genre => genre.genreName && genre.genreName.toLowerCase().includes(searchTerm));

            return titleMatch || authorMatch || genreMatch;
        });

        const startIndex = (currentPage - 1) * booksPerPage;
        const endIndex = startIndex + booksPerPage;
        const paginatedBooks = filteredBooks.slice(startIndex, endIndex);

        // Clear the list container before re-rendering the books
        const booksList = document.getElementById('books-list');
        booksList.innerHTML = '';

        // Populate the list with paginated books
        paginatedBooks.forEach(book => {
            const bookContainer = document.createElement('div');
            bookContainer.classList.add('book-container');

            let { authors, genres, id, price, published, stock, title } = book;

            bookContainer.innerHTML = `
                <h3>${title}</h3>
                <p><strong>Author:</strong> ${authors && authors.length > 0 ? authors[0].authorName : 'Unknown'}</p>
                <p><strong>Genre:</strong> ${genres && genres.length > 0 ? genres[0].genreName : 'Unknown'}</p>
                <p><strong>Price:</strong> ${price}</p>
                <p><strong>Stock:</strong> ${stock}</p>
                <p><strong>Published:</strong> ${published}</p>
                <div>
                    <input type="number" id="quantity-${id}" value="1" min="1" max="${stock}" ${stock === 0 ? 'disabled' : ''}>
                    <button class="add-to-cart-btn ${stock === 0 ? 'out-of-stock' : ''}" data-book-id="${id}" ${stock === 0 ? 'disabled' : ''}>${stock === 0 ? 'Out of Stock' : 'Add to Cart'}</button>
                    <button class="delete-book-btn" data-book-id="${id}">Delete</button>
                </div>
            `;
            booksList.appendChild(bookContainer);

            // Add to cart functionality
            const addToCartBtn = bookContainer.querySelector('.add-to-cart-btn');
            addToCartBtn.addEventListener('click', () => {
                const quantity = document.getElementById(`quantity-${id}`).value;
                addToCart(id, quantity, stock);
            });

            // Delete functionality
            const deleteBookBtn = bookContainer.querySelector('.delete-book-btn');
            deleteBookBtn.addEventListener('click', () => {
                deleteBook(id);
            });
        });

        // Update pagination controls
        const totalPages = Math.ceil(filteredBooks.length / booksPerPage);
        currentPageSpan.textContent = `Page ${currentPage} of ${totalPages}`;

        prevBtn.disabled = currentPage === 1;
        nextBtn.disabled = currentPage === totalPages;
    })
    .catch(error => console.error('Error fetching books:', error));
}

// Add a book to the cart
function addToCart(bookId, quantity, stock) {
    if (quantity > stock) {
        alert('Selected quantity exceeds available stock. Please select a lesser quantity.');
        return;
    }

    fetch(`${Url}/api/cart/add/${bookId}?quantity=${quantity}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' }
    })
    .then(response => response.json())
    .then(data => console.log('Book added to cart:', data))
    .catch(error => console.error('Error adding book to cart:', error));
}

// Delete a book and refresh the books list
function deleteBook(bookId) {
    fetch(`${Url}/books/${bookId}`, {
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' }
    })
    .then(response => {
        if (response.ok) {
            console.log(`Book with ID ${bookId} deleted successfully`);
            // Refresh the book list after deletion
            displayBooks();
        } else {
            console.error(`Error deleting book with ID ${bookId}`);
        }
    })
    .catch(error => console.error('Error deleting book:', error));
}

// Previous page button click handler
prevBtn.addEventListener('click', () => {
    if (currentPage > 1) {
        currentPage--;
        displayBooks();
    }
});

// Next page button click handler
nextBtn.addEventListener('click', () => {
    const totalPages = Math.ceil(allBooks.length / booksPerPage);
    if (currentPage < totalPages) {
        currentPage++;
        displayBooks();
    }
});

// Initial load of books
displayBooks();