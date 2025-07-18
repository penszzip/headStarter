import React from 'react'
import { useState, useEffect } from 'react'
import axios from 'axios'
import Contribute from './Contribute'

function Projects() {

	const [projects, setProjects] = useState([])
	const [isVisible, setIsVisible] = useState(false)
	const [selectedProject, setSelectedProject] = useState(null);

	const onClose = () => setIsVisible(false)

	useEffect(() => {
		axios.get('http://localhost:8080/projects')
			.then(response => {
				console.log(response)
				setProjects(response.data)
			})
	}, [])

	return (
		<div>
			<div className="max-w-1/2 m-auto min-h-screen p-6 mt-12">
				<h1 className="text-3xl text-left text-gray-700 mb-10">Explore the Projects</h1>
				<div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-20">
					{projects.map(project => (
						<div key={project.id} className="bg-white shadow-md rounded-lg p-4 flex flex-col justify-between">
							<div>
								<h2 className="text-xl font-semibold text-indigo-700">{project.name}</h2>
								<p className="text-gray-600 mt-2">{project.description}</p>
							</div>
							<div>
								<p className="text-sm text-gray-500 mt-4">Author: {project.author}</p>
								<p className="text-sm text-gray-500">Funding Goal: ${project.fundingGoal}</p>
								<p className="text-sm text-gray-500 mb-8">Current Funding: ${project.currentFunding}</p>
							</div>
							<div>
								<span className="p-2 border rounded-md border-indigo-500 bg-indigo-500 text-white cursor-pointer font-semibold hover:bg-gray-100 hover:text-indigo-500 transition duration-300 ease-in-out"
									onClick={() => setSelectedProject(project)}>
									Contribute
								</span>
							</div>
						</div>
					))}
				</div>

			</div>

			{selectedProject && (
        <Contribute
          project={selectedProject}
          onClose={() => setSelectedProject(null)}
        />
      )}
		</div>
	)
}

export default Projects