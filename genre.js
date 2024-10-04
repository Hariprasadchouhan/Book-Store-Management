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

function deleteGenre(genreId){
    fetch(`${Url}/genres/${genreId}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (response.ok) {
            console.log(`Author with ID ${genreId} deleted successfully`);
            // Remove the book from the table
            const row = document.querySelector(`tr[data-genre-id="${genreId}"]`);
            row.remove();
        } else {
            console.error(`Error deleting book with ID ${genreId}`);
        }
    })
    .catch(error => console.error(error));

}
function displayGenre(jsonData){
    const genresTbody = document.getElementById('genres-tbody');
    jsonData.forEach(genre => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${genre.genreName}</td>
            <td>
                <button class="delete-genre-btn" data-genre-id="${genre.genreId}"> Delete</button>
            </td>
        `;
        genresTbody.appendChild(row);

        const deleteBookBtn = row.querySelector('.delete-genre-btn');
            deleteBookBtn.addEventListener('click', () => {
            deleteGenre(genre.genreId);
        });
    });
};


async function displayGenres() {

    let url = `${Url}/genres`;
    let options = {
        mode:'cors',
        method: "GET"
    };
    fetch(url, options)
        .then(function(response) {
            return response.json();
        })
        .then(function(jsonData) {
            //displayGenres(jsonData);
          //  console.log(jsonData)
            displayGenre(jsonData)
        });
}
displayGenres()