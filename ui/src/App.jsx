import { useState, useEffect } from 'react'
import axios from 'axios'
import './App.css'

function App() {
  const [projects, setProjects] = useState([])

  useEffect(() => {
    axios.get('http://localhost:8080/projects')
          .then(response => {
            console.log(response)
            setProjects(response.data)
          })
  }, [])

  return (
    <>
      {projects.map(project => <h1>{project.name}</h1>)}
    </>
  )
}

export default App
