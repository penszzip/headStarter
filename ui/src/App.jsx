import Home from './components/Home'
import {
  BrowserRouter as Router,
  Routes, Route, Link
} from 'react-router-dom'
import NewProject from './components/NewProject'
import Login from './components/Login'
import SignUp from './components/SignUp'

function App() {

  const navlink = 'hover:text-indigo-300 transition duration-300 ease-in-out'
  const navButton = 'px-1.5 py-1 border rounded-md border-indigo-500 hover:bg-indigo-500 hover:border-indigo-500 hover:text-white transition duration-300 ease-in-out'

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
          <Link className={navButton} to="/login">
            Log in
          </Link>
          <Link className={navButton} to="/signup">Sign up</Link>
        </div>

      </nav>
      </div>

      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/projects/create" element={<NewProject />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<SignUp />} />
        <Route path="/logout" element={<Home />} />
      </Routes>
    </Router>
    </>
  )
}

export default App
