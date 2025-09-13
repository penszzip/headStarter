import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

function NewProject() {
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    author: '',
    fundingGoal: '',
    deadline: '',
  });
  const [files, setFiles] = useState(null);
  const token = localStorage.getItem('token');
  const navigate = useNavigate();

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleFileChange = (e) => {
    console.dir(e.target);
    setFiles(e.target.files);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const data = new FormData();
    if (files) {
      for (let i = 0; i < files.length; i++) {
        data.append('images', files[i])
      }
    }

    let deadlineWithTimezone = formData.deadline;
    if (deadlineWithTimezone && !deadlineWithTimezone.includes('Z') && 
        !deadlineWithTimezone.includes('+')) {
      deadlineWithTimezone = deadlineWithTimezone + ":00Z";
    }

    data.append('name', formData.name);
    data.append('description', formData.description);
    data.append('fundingGoal', formData.fundingGoal);
    data.append('deadline', deadlineWithTimezone);

    try {
      console.dir(formData)
      console.dir(data)
      const response = await axios.post('http://localhost:8080/projects', data, {
        headers: {
          'Content-Type': 'multipart/form-data',
          // Get the token from localStorage and use that in the request headers
          'Authorization': 'Bearer ' + token,
        },
      });
      console.log('Project created successfully:', response.data);
      navigate('/');
    } catch (error) {
      console.error('Error creating project:', error);
    }
  };

  return (
    <div className="max-w-2xl mx-auto p-6 mt-15 bg-white shadow-md rounded-md">
      <h1 className="text-2xl font-bold text-gray-700 mb-4">Create a New Project</h1>
      <form onSubmit={handleSubmit}>
        <div className="mb-4">
          <label className="block text-gray-700 font-medium mb-2">Project Name</label>
          <input
            type="text"
            name="name"
            value={formData.name}
            onChange={handleInputChange}
            className="w-full border border-gray-300 rounded-md p-2"
            required
          />
        </div>
        <div className="mb-4">
          <label className="block text-gray-700 font-medium mb-2">Description</label>
          <textarea
            name="description"
            value={formData.description}
            onChange={handleInputChange}
            className="w-full border border-gray-300 rounded-md p-2"
            required
          />
        </div>
        <div className="mb-4">
          <label className="block text-gray-700 font-medium mb-2">Funding Goal ($)</label>
          <input
            type="number"
            name="fundingGoal"
            value={formData.fundingGoal}
            onChange={handleInputChange}
            className="w-full border border-gray-300 rounded-md p-2"
            required
          />
        </div>
        <div className="mb-4">
          <label className="block text-gray-700 font-medium mb-2">Deadline</label>
          <input
            type="datetime-local"
            name="deadline"
            value={formData.deadline}
            onChange={handleInputChange}
            className="w-full border border-gray-300 rounded-md p-2"
            required
          />
        </div>
        <div className="mb-4">
          <label className="block text-gray-700 font-medium mb-2">Upload Image</label>
          <input
            type="file"
            multiple
            onChange={handleFileChange}
            className="w-full border border-gray-300 rounded-md p-2"
            required
          />
        </div>
        <button
          type="submit"
          className="w-full bg-indigo-500 text-white font-medium py-2 px-4 rounded-md hover:bg-indigo-600 transition"
        >
          Create Project
        </button>
      </form>
    </div>
  );
}

export default NewProject;