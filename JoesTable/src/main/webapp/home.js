function initMap() {
    var map = new google.maps.Map(document.getElementById('map'), {
        zoom: 4,
        center: {lat: 39.8283, lng: -98.5795} // Center the map over America
    });

    var marker;

    // Map click event
    map.addListener('click', function(e) {
        if (marker) {
            marker.setPosition(e.latLng);
        } else {
            // Create a new marker and place it on the map
            marker = new google.maps.Marker({
                position: e.latLng,
                map: map,
                draggable: true // Allow the marker to be dragged
            });
        }

        // Update the input fields with the new position
        document.getElementById('latitude').value = e.latLng.lat();
        document.getElementById('longitude').value = e.latLng.lng();
    });

    // Optional: If you want to allow dragging the marker and update the position after dragend
    if (marker) {
        google.maps.event.addListener(marker, 'dragend', function() {
            document.getElementById('latitude').value = marker.getPosition().lat();
            document.getElementById('longitude').value = marker.getPosition().lng();
        });
    }
}


var modal = document.getElementById("myModal");

// Get the button that opens the modal
var btn = document.getElementById("google-maps-button");

// Get the <span> element that closes the modal
var span = document.getElementsByClassName("close")[0];

// When the user clicks the button, open the modal 
btn.onclick = function() {
    modal.style.display = "block";
}

// When the user clicks on <span> (x), close the modal
span.onclick = function() {
    modal.style.display = "none";
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}

document.querySelector('.search-form').addEventListener('submit', function(e) {
    e.preventDefault(); // Prevent the default form submission

    var restaurantName = document.getElementById('restaurant-name').value;
    var latitude = document.getElementById('latitude').value;
    var longitude = document.getElementById('longitude').value;
    var radio = document.querySelector('input[name="sort"]:checked').value;

    // AJAX request to your server
    fetch('http://localhost:8080/JoesTable/search', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            term: restaurantName,
            latitude: latitude,
            longitude: longitude,
            sort: radio
        })
    })
    .then(response => response.json())
    .then(data => {
        // Handle the response data from your server
        displayResults(data); // Function to update the DOM with the results
    })
    .catch(error => {
        console.error('Error:', error);
    });
});

function displayResults(data) {
    // Hide the salmon image
    document.querySelector('.banner-image').style.display = 'none';
    
    // Container where results should be displayed
    var resultsContainer = document.getElementById('results-container');
    resultsContainer.innerHTML = ''; // Clear previous results

    // Loop through the data to create result elements
    data.forEach(business => {
        var resultElement = document.createElement('div');
        resultElement.classList.add('business-result'); // Add a class for styling

	    // Assuming the business object has 'name', 'image_url', and 'url' properties
	    var name = business.name;
	    var imageUrl = business.image_url;
	    var yelpUrl = business.url;
	    var address = business.display_address;
	    var phone = business.display_phone;
	    var price = business.price;
	    var category = business.category_title;
	
	    // Create the HTML structure
	    resultElement.innerHTML = `
	        <div class="business-result">
			    <div class="business-image">
			        <img src="${imageUrl}" alt="${name}" data-imageUrl= "${imageUrl}" data-name="${name}" data-address="${address}" data-phone="${phone}" data-rating="${rating}" data-category="${category}" data-yelp-url="${yelpUrl}" data-price="${price}" onclick="displayFullInfo(this)">
			    </div>
			    <div class="business-info">
			        <h2>${name}</h2>
			        <p class="business-address">${address}</p>
			        <a href="${yelpUrl}" target="_blank">View on Yelp</a>
			    </div>
			</div>
	    `;
	
	    // Append the result to the container
	    resultsContainer.appendChild(resultElement);
    });
}
function displayFullInfo(imageElement) {
    // Extract information from data attributes
    var imageUrl = imageElement.getAttribute('data-imageUrl');
    var name = imageElement.getAttribute('data-name');
    var address = imageElement.getAttribute('data-address');
    var phone = imageElement.getAttribute('data-phone');
    var yelpUrl = imageElement.getAttribute('data-yelp-url');
    var price = imageElement.getAttribute('data-price')
    var category = imageElement.getAttribute('data-category')
    
    // Reference to the main container
    var resultsContainer = document.getElementById('results-container');
    
    // Clear the container
    resultsContainer.innerHTML = '';
    // Create a new element to display the full information
    var fullInfo = document.createElement('div');
    fullInfo.classList.add('full-info');
    fullInfo.innerHTML = `
	    <div class="business-image">
	        <img src="${imageUrl}" alt="${name}">
	    </div>
	    <div class="business-info">
	        <h2>${name}</h2>
	        <p>Address: ${address}</p>
	        <p>Cuisine: ${category}</p>
	        <p>Phone: ${phone}</p>
	        <p>Price: ${price}</p>
	        <a href="${yelpUrl}" target="_blank">View on Yelp</a>
	    </div>
    `;

    // Append the new element to the container
    resultsContainer.appendChild(fullInfo);
    fetch('http://localhost:8080/JoesTable/homeServlet')
       .then(response => {
            return response.json(); // Use text() instead of json(), since the response is plain text
        })
        .then(data => {
            // Assuming the JSON object has a key 'userID' which holds the integer value
            const userID = data.userID;
            if (userID && userID > 0) {
		        var buttonsContainer = document.createElement('div');
		        buttonsContainer.className = 'button-container';
		
		        var favoritesButton = document.createElement('button');
		        favoritesButton.innerText = 'Add to Favorites';
		        favoritesButton.id = 'button1';
		        favoritesButton.onclick = function() {
		            function addFavorite() {
					    var data = {
					        imageUrl: imageUrl,
					        name: name,
					        address: address,
					        phone: phone,
					        yelpUrl: yelpUrl,
					        price: price,
					        category: category
					    };
					    const jsonData = JSON.stringify(data);
					
					    fetch('http://localhost:8080/JoesTable/addfavoriteServlet', {
					        method: 'POST',
					        headers: {
					            'Content-Type': 'application/json'
					        },
					        body: jsonData
					    })
					    .then(response => {
					        if (response.ok) {
					            return response.json();
					        } else {
					            return response.json().then(errorData => {
					                throw new Error(errorData);
					            });
					        }
					    })
					    .then(data => {
					        if (typeof data === 'number' && data === 1) {
					            favoritesButton.innerText = 'Remove Favorite';
					            favoritesButton.onclick = removeFavorite;
					        } else {
					            alert('Operation failed: ' + data);
					        }
					    })
					    .catch(error => {
					        console.error('Error:', error);
					        alert(error);
					    });
					}
					addFavorite();
					function removeFavorite() {
					    var data = {
					        name: name
					    };
					    const jsonData = JSON.stringify(data);
					
					    fetch('http://localhost:8080/JoesTable/removefavoriteServlet', {
					        method: 'POST',
					        headers: {
					            'Content-Type': 'application/json'
					        },
					        body: jsonData
					    })
					    .then(response => {
					        if(response.ok) {
					            return response.json();
					        } else {
					            return response.json().then(errorData => {
					                throw new Error(errorData);
					            });
					        }
					    })
					    .then(data => {
					        if (typeof data === 'number' && data === 1) {
					            favoritesButton.innerText = 'Add Favorite';
					            favoritesButton.onclick = addFavorite;
					        } else {
					            alert('Operation failed: ' + data);
					        }
					    })
					    .catch(error => {
					        console.error('Error:', error);
					        alert(error);
					    });
					}
				};
		
		        var reservationButton = document.createElement('button');
		        reservationButton.innerText = 'Add Reservation';
		        reservationButton.id = 'button2';
		        reservationButton.onclick = function() {
					var reservationForm = document.createElement('form');
		        	reservationForm.id = 'reservationForm';
					
					// Create a div to wrap the Date and Time inputs
					var dtDiv = document.createElement('div');
					dtDiv.id = 'DT';
					
					// Create the Date input with placeholder
					var dateInput = document.createElement('input');
					dateInput.type = 'date';
					dateInput.id = 'date';
					dateInput.name = 'date';
					dateInput.required = true;
					dateInput.placeholder = 'Date';  // Placeholder text instead of label
					
					// Create the Time input with placeholder
					var timeInput = document.createElement('input');
					timeInput.type = 'time';
					timeInput.id = 'time';
					timeInput.name = 'time';
					timeInput.required = true;
					timeInput.placeholder = 'Time';  // Placeholder text instead of label
					
					// Append the Date and Time inputs to the DT div
					dtDiv.appendChild(dateInput);
					dtDiv.appendChild(document.createElement('br')); // Line break for better formatting
					dtDiv.appendChild(timeInput);
					
					// Create the Reservation Notes textarea with placeholder
					var notesTextarea = document.createElement('textarea');
					notesTextarea.id = 'notes';
					notesTextarea.name = 'notes';
					notesTextarea.placeholder = 'Reservation Notes';  // Placeholder text instead of label
					
					// Create the Submit button
					var submitButton = document.createElement('button');
					submitButton.type = 'submit';
					submitButton.innerText = 'Submit Reservation';
					
					// Append the DT div and other elements to the form
					reservationForm.appendChild(dtDiv);
					reservationForm.appendChild(document.createElement('br')); // Line break for better formatting
					reservationForm.appendChild(notesTextarea);
					reservationForm.appendChild(document.createElement('br')); // Line break for better formatting
					reservationForm.appendChild(submitButton);
				    
				    reservationForm.onsubmit = function(event) {
				        event.preventDefault();
						var data = {
					        imageUrl: imageUrl,
					        name: name,
					        address: address,
					        phone: phone,
					        yelpUrl: yelpUrl,
					        price: price,
					        category: category
					    };
					    var formData = new FormData(reservationForm);
					    
					
					    for (const [key, value]  of formData.entries()) {
					        data[key] = value;
					    }
					    
					    var jsonObject = JSON.stringify(data);
					
					    fetch('http://localhost:8080/JoesTable/reservationServlet', { // Replace 'YourServletURL' with the actual URL of your servlet
				        method: 'POST',
				        headers: {
				            'Content-Type': 'application/json', 
				        },
				        body: jsonObject})
					    .then(response => {
					        return response.json();
					    })
					    .then(data => {
							if(data === "number" && data > 1){
								
							}
							
					        console.log(data);
					    })
					    .catch(error => {
					        console.error('Error:', error);
					    })
					    reservationForm.style.display = 'none';
						  
				    };
		    		buttonsContainer.appendChild(reservationForm);
		        };
			    // Append buttons to the buttons container
			    buttonsContainer.appendChild(favoritesButton);
			    buttonsContainer.appendChild(reservationButton);
			
			    // Finally, append the buttons container to the fullInfo div
			    resultsContainer.appendChild(buttonsContainer);
			}

        })
        .catch(error => {
            console.error('There has been a problem with your fetch operation:', error);
        });
}
function updateUserInterface() {
    // Replace 'yourServletURL' with the actual URL of your servlet
    fetch('http://localhost:8080/JoesTable/homeServlet')
       .then(response => {
            return response.json(); // Use text() instead of json(), since the response is plain text
        })
        .then(data => {
            // Parse the response text to an integer
            const userID = data.userID;

            let navContent;
            if (userID > 0) {
                // User is logged in
                navContent = `
                    <ul>
                        <li><a href="home.html">Home</a></li>
                        <li><a href="favorites.html">Favorites</a></li>
                        <li><a href="reservations.html">Reservations</a></li>
                        <li><a href="http://localhost:8080/JoesTable/logoutServlet">Logout</a></li>
                    </ul>`;
            } else {
                // User is not logged in
                navContent = `
                    <ul>
                        <li><a href="home.html">Home</a></li>
                        <li><a href="login.html">Login / Sign Up</a></li>
                    </ul>`;
            }

            // Update the navigation content
            document.querySelector('header nav').innerHTML = navContent;
        })
        .catch(error => {
            console.error('There has been a problem with your fetch operation:', error);
            // Optionally update the UI to reflect the error
        });
}

// Call the function when the page loads
window.onload = updateUserInterface;
