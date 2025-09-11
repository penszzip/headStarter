import React from 'react'
import { useState, useEffect } from 'react'
import axios from 'axios'
import Contribute from './Contribute'
import { Carousel } from 'react-responsive-carousel'
import "react-responsive-carousel/lib/styles/carousel.min.css"; // Import carousel styles
import { useNavigate } from 'react-router-dom'

function Projects() {

	const [projects, setProjects] = useState([])
	const [selectedProject, setSelectedProject] = useState(null);
	const navigate = useNavigate();

	const onClose = () => setIsVisible(false);

	const redirect = () => navigate('/contribute');

	useEffect(() => {
		axios.get('http://localhost:8080/projects')
			.then(response => {
				console.log(response)
				setProjects(response.data)
			})
	}, [])

	return (
		<div>
			<div className="max-w-1/2 m-auto min-h-screen p-6 mt-11">
				<h1 className="text-3xl text-left text-gray-700 mb-10">Explore the Projects</h1>
				<div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-2 gap-20">
					{projects.map(project => (
						<div key={project.id} className="bg-white shadow-md rounded-lg p-4 flex flex-col justify-between hover:bg-gray-100 transition cursor-pointer" 
							onClick={() => navigate(`/contribute/${project.id}`, { state: { project } })}>
							{/* Image Carousel */}
							{project.images && project.images.length > 0 && (
								<div className="mb-4">
								<Carousel 
									showArrows={true}
									showStatus={false}
									showThumbs={false}
									infiniteLoop={true}
									autoPlay={false}
									className="rounded-lg overflow-hidden"
								>
									{project.images.map((imageUrl, index) => (
									<div key={index}>
										<img 
										src={imageUrl} 
										alt={`${project.name} - image ${index + 1}`} 
										className="h-96 w-full object-cover"
										/>
									</div>
									))}
								</Carousel>
								</div>
							)}
							<div>
								<h2 className="text-xl font-semibold text-indigo-700">{project.name}</h2>
								<p className="text-gray-600 mt-2">{project.description}</p>
							</div>
							<div>
								<p className="text-sm text-gray-500 mt-4">Author: {project.author}</p>
								<p className="text-sm text-gray-500">Funding Goal: ${project.fundingGoal}</p>
								<p className="text-sm text-gray-500 mb-8">Current Funding: ${project.currentFunding}</p>
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