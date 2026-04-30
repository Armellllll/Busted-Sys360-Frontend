import { createContext, useContext, useState } from 'react';
import type { ReactNode } from 'react';

export interface Inmate {
  id: string;
  lastName: string;
  firstName: string;
  dob: string;
  age: number;
  sex: string;
  raceSexAge: string;
  location: string;
  cellule?: string;
  arrested: string;
  sentenceDuration: string;
  released: string;
  releaseStatus: 'safe' | 'warning' | 'danger';
  photoUrl?: string;
  sanctions?: { date: string; description: string }[];
}

interface InmatesDataContextType {
  inmates: Inmate[];
  isLoading: boolean;
  setInmates: (inmates: Inmate[]) => void;
  addInmate: (inmate: Omit<Inmate, 'id'>) => Promise<void>;
  refreshInmates: () => Promise<void>;
  updateInmatePhoto: (id: string, photoUrl: string) => void;
}

const defaultInmates: Inmate[] = [
  { 
    id: '1', 
    lastName: 'CARTER', 
    firstName: 'Jason Aaron',
    dob: '02/14/1996',
    age: 30,
    sex: 'Male',
    raceSexAge: 'W/M/30 years',
    location: 'Judsonia, AR',
    cellule: 'A-101',
    arrested: '18/07/2026 00:45',
    sentenceDuration: '5 ans',
    released: '10/12/2031',
    releaseStatus: 'safe',
    sanctions: [{ date: '01/05/2026', description: 'Altération mineure' }]
  },
  { 
    id: '2', 
    lastName: 'Armstrong', 
    firstName: 'Chon Elex',
    dob: '05/20/1995',
    age: 31,
    sex: 'Male',
    raceSexAge: 'W/M/31 years',
    location: 'Pocahontas, AR',
    cellule: 'B-205',
    arrested: '18/07/2026 10:20',
    sentenceDuration: '1 an',
    released: '15/08/2026',
    releaseStatus: 'warning',
    sanctions: []
  },
  { 
    id: '3', 
    lastName: 'Duren', 
    firstName: 'Christopher',
    dob: '11/04/1994',
    age: 32,
    sex: 'Male',
    raceSexAge: 'W/M/32 years',
    location: 'Searcy, AR',
    cellule: 'A-102',
    arrested: '18/07/2026 00:00',
    sentenceDuration: '6 mois',
    released: '01/05/2026',
    releaseStatus: 'danger',
    sanctions: [{ date: '03/12/2026', description: 'Bagarre dans la cour' }]
  },
  { 
    id: '4', 
    lastName: 'Valencia', 
    firstName: 'Jose M',
    dob: '01/30/1986',
    age: 40,
    sex: 'Male',
    raceSexAge: 'U/M/40 years',
    location: 'Bradford, AR',
    cellule: 'C-080',
    arrested: '18/07/2026 00:34',
    sentenceDuration: 'Perpétuité',
    released: 'Non libéré',
    releaseStatus: 'safe',
    sanctions: []
  }
];

const InmatesDataContext = createContext<InmatesDataContextType | undefined>(undefined);

export const InmatesDataProvider = ({ children }: { children: ReactNode }) => {
  const [inmates, setInmates] = useState<Inmate[]>(defaultInmates);
  const [isLoading, setIsLoading] = useState(false);

  /**
   * addInmate — simule un appel POST vers le backend.
   * Quand le vrai backend sera prêt, remplacer le bloc simulé
   * par: const saved = await api.post('/inmates', inmate)
   * puis appeler refreshInmates() pour récupérer la liste à jour.
   */
  const addInmate = async (inmate: Omit<Inmate, 'id'>): Promise<void> => {
    setIsLoading(true);
    // --- Simulation backend (à remplacer par fetch réel) ---
    await new Promise(resolve => setTimeout(resolve, 600));
    const newId = String(Date.now());
    const saved: Inmate = { ...inmate, id: newId };
    // -------------------------------------------------------
    // Mise à jour globale via updater fonctionnel (jamais de stale state)
    setInmates(prev => [...prev, saved]);
    setIsLoading(false);
  };

  /**
   * refreshInmates — simule un appel GET /inmates vers le backend.
   * Quand le vrai backend sera prêt, remplacer par:
   * const data = await api.get('/inmates')
   * setInmates(data)
   */
  const refreshInmates = async (): Promise<void> => {
    setIsLoading(true);
    // --- Simulation backend (à remplacer par fetch réel) ---
    await new Promise(resolve => setTimeout(resolve, 400));
    // La liste est déjà à jour en mémoire, rien à remplacer en mode mock
    // -------------------------------------------------------
    setIsLoading(false);
  };

  const updateInmatePhoto = (id: string, photoUrl: string) => {
    setInmates(prev => prev.map(inmate => 
      inmate.id === id ? { ...inmate, photoUrl } : inmate
    ));
  };

  return (
    <InmatesDataContext.Provider value={{ inmates, isLoading, setInmates, addInmate, refreshInmates, updateInmatePhoto }}>
      {children}
    </InmatesDataContext.Provider>
  );
};

export const useInmatesData = () => {
  const context = useContext(InmatesDataContext);
  if (context === undefined) {
    throw new Error("useInmatesData doit être utilisé au sein d'un InmatesDataProvider");
  }
  return context;
};
