
document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault();
    const formData = new FormData(document.getElementById('loginForm'));
	const data = {};
	formData.forEach((value, key) => { data[key] = value;});
	const jsonData = JSON.stringify(data);
	console.log(jsonData);
    fetch('http://localhost:8080/JoesTable/loginServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: jsonData
    }).then(response => {
	    console.log("HTTP Status Code: ", response.status);
	
	    if (response.ok) { // response.ok is true for status codes in the range 200-299
	        return response.json(); // or .text() if you're expecting a text response
	    } else {
        // For error response, parse the response and throw an error
        	return response.json().then(errorData => {
            throw new Error(errorData);
        });
    }
	}) 
    .then(data => {
        if (typeof data === 'number') {
            window.location.href = 'home.html';
        } else {
            alert('Login failed: ' + data);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert(error);
    });
});

document.getElementById('signupForm').addEventListener('submit', function(event) {
    event.preventDefault();

    // Get the password and confirm password values
    var password = document.getElementById('newPassword').value;
    var confirmPassword = document.getElementById('confirmPassword').value;

    // Check if passwords match
    if (password !== confirmPassword) {
        alert('Passwords do not match.');
        return; // Stop further execution if passwords don't match
    }

    const formData = new FormData(document.getElementById('signupForm'));
	const data = {};
	formData.forEach((value, key) => {
	    if (key !== 'confirmPassword' && key !== 'terms') {
	        data[key] = value;
	    }
	});
	const jsonData = JSON.stringify(data);
	console.log(jsonData);
    fetch('http://localhost:8080/JoesTable/signupServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: jsonData
    })
    .then(response => {
	    console.log("HTTP Status Code: ", response.status);
	
	    if (response.ok) { // response.ok is true for status codes in the range 200-299
	        return response.json(); // or .text() if you're expecting a text response
	    } else {
        // For error response, parse the response and throw an error
        	return response.json().then(errorData => {
            throw new Error(errorData);
        });
    }
	}) 
    .then(data => {
        if (typeof data === 'number') {
            window.location.href = 'home.html';
        } else {
            alert('Signup failed: ' + data);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert(error);
    });
});
