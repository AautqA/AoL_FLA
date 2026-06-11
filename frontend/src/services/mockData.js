// Mock Data for Development (Frontend without Backend)

export const mockUsers = {
  'customer@example.com': {
    user_id: 1,
    name: 'John Customer',
    email: 'customer@example.com',
    password: 'password123',
    role: 'customer'
  },
  'admin@example.com': {
    user_id: 2,
    name: 'Admin User',
    email: 'admin@example.com',
    password: 'admin123',
    role: 'admin'
  }
};

export const mockTables = [
  {
    table_id: 1,
    restaurant_name: 'Savor House',
    table_number: 1,
    capacity: 2,
    status: 'Available'
  },
  {
    table_id: 2,
    restaurant_name: 'Savor House',
    table_number: 2,
    capacity: 4,
    status: 'Available'
  },
  {
    table_id: 3,
    restaurant_name: 'Ember Grill',
    table_number: 3,
    capacity: 4,
    status: 'Available'
  },
  {
    table_id: 4,
    restaurant_name: 'Ocean Plate',
    table_number: 4,
    capacity: 6,
    status: 'Available'
  },
  {
    table_id: 5,
    restaurant_name: 'Cedar Garden',
    table_number: 5,
    capacity: 6,
    status: 'Available'
  },
  {
    table_id: 6,
    restaurant_name: 'Cedar Garden',
    table_number: 6,
    capacity: 8,
    status: 'Available'
  }
];

export const mockReservations = [
  {
    reservation_id: 1,
    user_id: 1,
    table_id: 1,
    booking_date: '2026-05-25',
    booking_time: '19:00',
    people_count: 2,
    status: 'confirmed',
    restaurant_name: 'Savor House',
    table_number: 1,
    table_capacity: 2
  },
  {
    reservation_id: 2,
    user_id: 1,
    table_id: 2,
    booking_date: '2026-05-26',
    booking_time: '20:00',
    people_count: 4,
    status: 'confirmed',
    restaurant_name: 'Savor House',
    table_number: 2,
    table_capacity: 4
  }
];

// Simulate API responses
export const mockAuthService = {
  register: async (name, email, password) => {
    // Simulate delay
    await new Promise(resolve => setTimeout(resolve, 500));
    
    if (mockUsers[email]) {
      throw new Error('Email already registered');
    }
    
    const newUser = {
      user_id: Math.max(...Object.values(mockUsers).map(u => u.user_id)) + 1,
      name,
      email,
      password,
      role: 'customer'
    };
    
    mockUsers[email] = newUser;
    
    return {
      user: {
        user_id: newUser.user_id,
        name: newUser.name,
        email: newUser.email,
        role: newUser.role
      },
      token: 'mock_token_' + Date.now()
    };
  },
  
  login: async (email, password) => {
    // Simulate delay
    await new Promise(resolve => setTimeout(resolve, 500));
    
    const user = mockUsers[email];
    
    if (!user || user.password !== password) {
      throw new Error('Invalid email or password');
    }
    
    return {
      user: {
        user_id: user.user_id,
        name: user.name,
        email: user.email,
        role: user.role
      },
      token: 'mock_token_' + Date.now()
    };
  }
};

export const mockTableService = {
  getAvailableTables: async (date, time) => {
    await new Promise(resolve => setTimeout(resolve, 300));
    return { data: mockTables.filter(t => t.status === 'Available') };
  },
  
  getAllTables: async () => {
    await new Promise(resolve => setTimeout(resolve, 300));
    return { data: mockTables };
  },
  
  getTableById: async (id) => {
    await new Promise(resolve => setTimeout(resolve, 200));
    return { data: mockTables.find(t => t.table_id === parseInt(id)) };
  },
  
  addTable: async (restaurantName, tableNumber, capacity) => {
    await new Promise(resolve => setTimeout(resolve, 300));
    
    const newTable = {
      table_id: Math.max(...mockTables.map(t => t.table_id)) + 1,
      restaurant_name: restaurantName || 'DineReserve Bistro',
      table_number: tableNumber,
      capacity,
      status: 'Available'
    };
    
    mockTables.push(newTable);
    return { data: newTable };
  },
  
  updateTable: async (id, restaurantName, tableNumber, capacity, status) => {
    await new Promise(resolve => setTimeout(resolve, 300));
    
    const table = mockTables.find(t => t.table_id === parseInt(id));
    if (!table) throw new Error('Table not found');
    
    table.restaurant_name = restaurantName || table.restaurant_name;
    table.table_number = tableNumber;
    table.capacity = capacity;
    table.status = status;
    
    return { data: table };
  },
  
  deleteTable: async (id) => {
    await new Promise(resolve => setTimeout(resolve, 300));
    
    const index = mockTables.findIndex(t => t.table_id === parseInt(id));
    if (index === -1) throw new Error('Table not found');
    
    const deleted = mockTables.splice(index, 1)[0];
    return { data: deleted };
  }
};

export const mockReservationService = {
  createReservation: async (userId, tableId, date, time, peopleCount) => {
    await new Promise(resolve => setTimeout(resolve, 500));
    
    const table = mockTables.find(t => t.table_id === parseInt(tableId));
    if (!table) throw new Error('Table not found');
    if (table.capacity < peopleCount) throw new Error('Table capacity too small');
    
    const newReservation = {
      reservation_id: Math.max(...mockReservations.map(r => r.reservation_id), 0) + 1,
      user_id: userId,
      table_id: parseInt(tableId),
      restaurant_name: table.restaurant_name,
      booking_date: date,
      booking_time: time,
      people_count: peopleCount,
      status: 'confirmed',
      table_number: table.table_number,
      table_capacity: table.capacity
    };
    
    mockReservations.push(newReservation);
    return { data: newReservation };
  },
  
  cancelReservation: async (id) => {
    await new Promise(resolve => setTimeout(resolve, 300));
    
    const reservation = mockReservations.find(r => r.reservation_id === parseInt(id));
    if (!reservation) throw new Error('Reservation not found');
    
    reservation.status = 'cancelled';
    return { data: reservation };
  },
  
  getReservationHistory: async (userId) => {
    await new Promise(resolve => setTimeout(resolve, 300));
    
    const userReservations = mockReservations.filter(r => r.user_id === parseInt(userId));
    return { data: userReservations };
  },
  
  getAllReservations: async () => {
    await new Promise(resolve => setTimeout(resolve, 300));
    return { data: mockReservations };
  },
  
  getReservationById: async (id) => {
    await new Promise(resolve => setTimeout(resolve, 200));
    return { data: mockReservations.find(r => r.reservation_id === parseInt(id)) };
  },
  
  checkAvailability: async (tableId, date, time) => {
    await new Promise(resolve => setTimeout(resolve, 200));
    
    const reservation = mockReservations.find(
      r => r.table_id === parseInt(tableId) && 
           r.booking_date === date && 
           r.booking_time === time &&
           r.status === 'confirmed'
    );
    
    return { data: { available: !reservation, table_id: tableId, date, time } };
  }
};
