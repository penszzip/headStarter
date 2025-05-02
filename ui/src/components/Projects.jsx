import React from 'react'
import { useState, useEffect } from 'react'
import axios from 'axios'

function Projects() {

	const [projects, setProjects] = useState([])

	useEffect(() => {
		axios.get('http://localhost:8080/projects')
			.then(response => {
				console.log(response)
				setProjects(response.data)
			})
	}, [])

	return (
		<div className="max-w-1/2 m-auto min-h-screen p-6 mt-12">
      <h1 className="text-3xl font-bold text-left text-indigo-600 mb-10">Explore the Projects</h1>
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
        {projects.map(project => (
          <div key={project.id} className="bg-white shadow-md rounded-lg p-4">
            <h2 className="text-xl font-semibold text-indigo-700">{project.name}</h2>
            <p className="text-gray-600 mt-2">{project.description}</p>
            <p className="text-sm text-gray-500 mt-4">Author: {project.author}</p>
            <p className="text-sm text-gray-500">Funding Goal: ${project.fundingGoal}</p>
            <p className="text-sm text-gray-500 mb-2">Current Funding: ${project.currentFunding}</p>
						<span className='p-2 border rounded-md border-gray-400 bg-indigo-500 text-white'>Contribute</span>
          </div>
        ))}
      </div>
    </div>
	)
}

export default Projects