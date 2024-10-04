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


let Url='http://localhost:8080'


function deleteAuthor(authorId){
    fetch(`${Url}/authors/${authorId}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (response.ok) {
            console.log(`Author with ID ${authorId} deleted successfully`);
            // Remove the book from the table
            const row = document.querySelector(`tr[data-author-id="${authorId}"]`);
            row.remove();
        } else {
            console.error(`Error deleting book with ID ${authorId}`);
        }
    })
    .catch(error => console.error(error));

}
function displayAuthor(jsonData){  
    const authorsTbody = document.getElementById('authors-tbody');
    jsonData.forEach(author => {
        const row = document.createElement('tr');
        console.log(author.authorId)
        row.innerHTML = `
            <td>${author.authorName}</td>
            
            <td>
                <button class="delete-author-btn" data-author-id="${author.id}"> Delete</button>
            </td>
        `;
        authorsTbody.appendChild(row);

        const deleteBookBtn = row.querySelector('.delete-author-btn');
            deleteBookBtn.addEventListener('click', () => {
            deleteAuthor(author.authorId);
        });
    });
};
async function displayAuthors() {

    let url = `${Url}/authors`;
    let options = {
        method: "GET"
    };
    fetch(url, options)
        .then(function(response) {
            return response.json();
        })
        .then(function(jsonData) {
            //displayGenres(jsonData);
           // console.log(jsonData)
            displayAuthor(jsonData)
        });
}
displayAuthors()