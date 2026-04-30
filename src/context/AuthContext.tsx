import { createContext, useContext, useState } from 'react';
import type { ReactNode } from 'react';

export type Role = 'agent' | 'responsable_visite' | 'directeur' | 'greffier' | 'service_medical';

interface AuthContextType {
  isAuthenticated: boolean;
  role?: Role;
  login: (username: string, password?: string) => void;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
  const [role, setRole] = useState<Role | undefined>(undefined);

  const login = (username: string) => {
    console.log(`Authentification réussie pour : ${username}`);
    setIsAuthenticated(true);
    if (['agent', 'responsable_visite', 'directeur', 'greffier', 'service_medical'].includes(username)) {
      setRole(username as Role);
    }
  };

  const logout = () => {
    setIsAuthenticated(false);
    setRole(undefined);
  };

  return (
    <AuthContext.Provider value={{ isAuthenticated, role, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error("useAuth doit être utilisé au sein d'un AuthProvider");
  }
  return context;
};
