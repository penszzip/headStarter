import './App.css'
import Home from './components/Home'
import {
  BrowserRouter as Router,
  Routes, Route, Link
} from 'react-router-dom'

function App() {

  const navlink = 'hover:text-indigo-500 transition duration-300 ease-in-out'

  return (
    <>
    <Router>
      <div className='bg-gray-100'>
      <nav className="flex items-center justify-between p-4 text-indigo-600 max-w-3/4 m-auto text-lg">

        <div className="text-xl font-bold">
          HeadStarter
        </div>

        <div className='flex space-x-11 font-medium'>
          <Link to="/" className={navlink}>Explore</Link>
          <Link to="/projects/create" className={navlink}>Create a project</Link>
        </div>

        <div className='flex space-x-4 font-medium'>
          <Link className='px-1.5 py-1 border rounded-md border-gray-400 hover:bg-indigo-500 hover:border-indigo-500 hover:text-white transition duration-300 ease-in-out' to="/login">
            Log in
          </Link>
          {/* <Link to="/logout">Log out</Link> */}
        </div>

      </nav>
      </div>

      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/projects/create" element={<Home />} />
        <Route path="/login" element={<Home />} />
        <Route path="/logout" element={<Home />} />
        <Route path="/projects/:id/contribute" element={<Home />} />
      </Routes>
    </Router>

    
    </>
  )
}

export default App
