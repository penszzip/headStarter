import Home from './components/Home'
import {
  BrowserRouter as Router,
  Routes, Route, Link, Navigate
} from 'react-router-dom'
import NewProject from './components/NewProject'
import Login from './components/Login'
import SignUp from './components/SignUp'
import { AuthProvider, AuthContext } from './context/AuthContext'
import { useContext } from 'react'
import Contribute from './components/Contribute'
import Success from './components/Success'
import Failure from './components/Failure'

// Protected route component
const ProtectedRoute = ({ children }) => {
  const { isAuthenticated, loading } = useContext(AuthContext);
  
  if (loading) return <div>Loading...</div>;
  
  if (!isAuthenticated) {
    return <Navigate to="/login" />;
  }
  
  return children;
};

function AppContent() {
  const { isAuthenticated, logout } = useContext(AuthContext);

  const navlink = 'hover:text-indigo-300 transition duration-300 ease-in-out'
  const navButton = 'px-1.5 py-1 border rounded-md border-indigo-500 hover:bg-indigo-500 hover:border-indigo-500 hover:text-white transition duration-300 ease-in-out'

  return (
    <>
    <Router>
      <div className='bg-gray-100'>
      <nav className="flex items-center justify-between p-4 text-indigo-600 max-w-3/4 m-auto text-lg">

        <div className="text-3xl font-bold">
          HeadStarter
        </div>

        <div className='flex space-x-11 font-medium'>
          <Link to="/" className={navlink}>Explore</Link>
          {isAuthenticated && (
            <Link to="/projects/create" className={navlink}>Create a project</Link>
          )}
        </div>

        <div className='flex space-x-4 font-medium'>
          {!isAuthenticated ? (
            <>
              <Link className={navButton} to="/login">
                Log in
              </Link>
              <Link className={navButton} to="/signup">Sign up</Link>
            </>
          ) : (
            <button 
              className={navButton}
              onClick={logout}
            >
              Log out
            </button>
          )}
        </div>

      </nav>
      </div>

      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/projects/create" element={
          <ProtectedRoute>
            <NewProject />
          </ProtectedRoute>
        } />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<SignUp />} />
        <Route path="/contribute/:projectId" element={<Contribute />} />
        <Route path="/success" element={<Success />} />
        <Route path="/failure" element={<Failure />} />

      </Routes>
    </Router>
    </>
  )
}

function App() {
  return (
    <AuthProvider>
        <AppContent />
    </AuthProvider>
  )
}

export default App
