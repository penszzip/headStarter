import React, { useEffect, useState } from 'react';

function Contribute({ project, onClose }) {
	const [amount, setAmount] = useState(0);

	const handleContribute = () => {
		onContribute(amount);
		onClose();
	};

	useEffect(() => {
    document.body.style.overflow = 'hidden'; // Disable scrolling
    return () => {
      document.body.style.overflow = 'auto'; // Re-enable scrolling when the component unmounts
    };
  }, []);

	return (
		<div className="fixed inset-0 flex items-center justify-center z-50">
			
			<div
				className="absolute inset-0 bg-black opacity-50"
				onClick={onClose} // Close the overlay when clicking outside the modal
			></div>

			<div className="relative bg-white rounded-lg shadow-lg p-6 w-96">
				<h2 className="text-xl font-semibold text-indigo-700 mb-4">
					Contribute to {project.name}
				</h2>
				<p className="text-gray-600 mb-6">{project.description}</p>
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
						onClick={onClose}
						className="px-4 py-2 bg-gray-300 rounded-md hover:bg-gray-400 transition"
					>
						Cancel
					</button>
					<button
						className="px-4 py-2 bg-indigo-500 text-white rounded-md hover:bg-indigo-600 transition"
					>
						Contribute
					</button>
				</div>
			</div>
		</div>
	);
}

export default Contribute;