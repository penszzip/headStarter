import React, { useEffect, useState, useContext } from 'react';
import { useParams, useNavigate, useLocation } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import axios from 'axios';

// function Contribute({ project, onClose }) {
function Contribute() {

	const { projectId } = useParams();
	const location = useLocation();
	const navigate = useNavigate();
	const token = localStorage.getItem('token');

	const [project, setProject] = useState(location.state?.project || null);
	const [amount, setAmount] = useState(0);
	const [name, setName] = useState("");
    const [email, setEmail] = useState("");
	const [loading, setLoading] = useState(!project);
	const [submitting, setSubmitting] = useState(false);
	const { isAuthenticated } = useContext(AuthContext);

	useEffect(() => {
		if (!isAuthenticated) {
			navigate('/login');
  		}
		setLoading(true);
		axios.get(`http://localhost:8080/projects/${projectId}`)
		.then(response => {
			setProject(response.data);
			setLoading(false);
		})
		.catch(error => {
			console.error('Error fetching project:', error);
			setLoading(false);
		});
	}, [project, projectId]);

	const handleContribute = () => {
		if (amount <= 0) {
			alert("Please enter a valid amount");
			return;
		}
		
		if (!name.trim()) {
			alert("Please enter your name");
			return;
		}
		
		if (!email.trim() || !email.includes('@')) {
			alert("Please enter a valid email");
			return;
		}

		setSubmitting(true);
		initiatePayment();
	};

	const initiatePayment = async () => {
		try {
			// Send checkout request to backend
			console.log(token);
			const response = await axios.post("http://localhost:8080/checkout/hosted", {
				projectId: parseInt(projectId),
				amount: parseInt(amount),
				customerName: name,
				customerEmail: email,
			}, { headers: {
          		// Get the token from localStorage and use that in the request headers
          		'Authorization': 'Bearer ' + token,
        	}});
			// Redirect to stripe if successful
			window.location.href = response.data.url;
		} catch (error) {
			console.error('Error creating checkout session:', error);
			alert('Something went wrong. Please try again.');
			setSubmitting(false);
		}
    }

	return (
		<div className="w-full max-w-md mx-auto mt-30 p-6 bg-white rounded-lg shadow-md">
			<h2 className="text-2xl font-semibold text-indigo-700 mb-4">
				Contribute to {project.name}
			</h2>
			<label className="block text-gray-700 font-medium mb-2">
				Name:
			</label>
			<input
				type="text"
				value={name}
				onChange={(e) => setName(e.target.value)}
				className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:border-indigo-500 mb-2"
			/>
			<label className="block text-gray-700 font-medium mb-2">
				Email:
			</label>
			<input
				type="text"
				value={email}
				onChange={(e) => setEmail(e.target.value)}
				className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:border-indigo-500 mb-2"
			/>
			<label className="block text-gray-700 font-medium mb-2">
				Choose an amount:
			</label>
			<input
				type="range"
				min="1"
				max={100}
				value={amount}
				onChange={(e) => setAmount(e.target.value)}
				className="w-full"
			/>
			<p className="text-gray-700 mt-2">Amount: ${amount}</p>
			<div className="flex justify-end mt-6 space-x-4">
				<button
					// onClick={onClose}
					className="px-4 py-2 bg-gray-300 rounded-md hover:bg-gray-400 transition"
				>
					Cancel
				</button>
				<button
					className={`px-4 py-2 bg-indigo-500 text-white rounded-md hover:bg-indigo-600 transition ${submitting ? 'opacity-50 cursor-not-allowed' : ''}`}
					onClick={handleContribute} disabled={submitting}
				>
					{submitting ? 'Processing...' : 'Contribute'}
				</button>
			</div>
		</div>
	);
}

export default Contribute;