import React from 'react'
import { useNavigate } from 'react-router-dom';

const Success = () => {

	const navigate = useNavigate();

	const onButtonClick = () => {
		navigate('/');
	}
	return (
		<div className="flex flex-col items-center justify-center h-[calc(100vh-200px)]">
            <div className='text-4xl font-bold text-green-600 mb-8'>Success</div>
            <button 
                onClick={onButtonClick}
                className="px-8 py-4 text-xl font-bold text-white bg-green-600 rounded-lg shadow-md hover:bg-green-700 transition-colors duration-300"
            >
                Go to home page
            </button>
        </div>
	)
}

export default Success