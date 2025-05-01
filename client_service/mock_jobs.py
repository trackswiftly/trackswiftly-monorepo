import json
import random
import sys
from math import radians, sin, cos, sqrt, atan2, asin, degrees

# Moroccan cities with coordinates [longitude, latitude]
MOROCCAN_CITIES = [
    {"name": "Casablanca", "location": [-7.589843, 33.573110]},
    {"name": "Marrakech", "location": [-7.981205, 31.629472]},
    {"name": "Rabat", "location": [-6.832550, 34.020882]},
    {"name": "Fes", "location": [-4.987995, 34.025299]},
    {"name": "Tangier", "location": [-5.813629, 35.759465]},
    {"name": "Agadir", "location": [-9.598107, 30.427755]},
    {"name": "Meknes", "location": [-5.556320, 33.886917]},
    {"name": "Oujda", "location": [-1.907517, 34.681962]},
    {"name": "Kenitra", "location": [-6.578611, 34.261538]},
    {"name": "Tetouan", "location": [-5.368860, 35.576218]},
    {"name": "Safi", "location": [-9.237654, 32.283155]},
    {"name": "Oulad Aissa", "location": [-8.607788, 32.953545]},
]

ROAD_INTERSECTIONS = [
    # Oulad Aissa area coordinates (latitude, longitude)
    (32.953545, -8.607788),  # Base location
    (32.960000, -8.600000),
    (32.950000, -8.620000),
    (32.940000, -8.590000),
    (32.965000, -8.610000),
    # Safi area coordinates
    (32.283155, -9.237654),
    (32.290000, -9.230000),
    (32.275000, -9.240000),
    # Youssoufia coordinates
    (32.246891, -8.639324),
    (32.250000, -8.635000),
    # Add 50+ more verified coordinates...
]

def generate_routable_point():
    # Select a known good road point and add small offset
    base_lat, base_lon = random.choice(ROAD_INTERSECTIONS)
    return [
        round(base_lat + random.uniform(-0.01, 0.01), 6),  # ~1km variance
        round(base_lon + random.uniform(-0.01, 0.01), 6)
    ]

def generate_random_point(base_lon, base_lat, max_km):
    # Generate random points within max_km distance
    while True:
        # Random bearing and distance
        bearing = radians(random.uniform(0, 360))
        distance = random.uniform(0, max_km)
        
        # Convert to radians
        lat1 = radians(base_lat)
        lon1 = radians(base_lon)
        
        # Calculate new coordinates
        ang_dist = distance / 6371
        lat2 = asin(sin(lat1) * cos(ang_dist) + 
               cos(lat1) * sin(ang_dist) * cos(bearing))
        lon2 = lon1 + atan2(sin(bearing) * sin(ang_dist) * cos(lat1),
                           cos(ang_dist) - sin(lat1) * sin(lat2))
        
        # Convert back to degrees
        new_lat = degrees(lat2)
        new_lon = degrees(lon2)
        
        # Verify distance (should always be true)
        if haversine(base_lon, base_lat, new_lon, new_lat) <= max_km:
            return [new_lon, new_lat]



def generate_mock_data(num_jobs, num_vehicles, max_distance_km):
    vehicle_base = next(c for c in MOROCCAN_CITIES if c["name"] == "Oulad Aissa")
    base_lon, base_lat = vehicle_base["location"]
    
    jobs = []
    for job_id in range(1, num_jobs + 1):
        # Keep trying until we get a valid point
        while True:
            location = generate_routable_point()
            distance = haversine(base_lon, base_lat, location[1], location[0])
            if distance <= max_distance_km:
                break
                
        jobs.append({
            "id": job_id,
            "description": f"Location {job_id}",
            "location": [location[1], location[0]],  # Valhalla expects [lon, lat]
            "delivery": [1]
        })


# Modified Haversine to use correct coordinate order
def haversine(lon1, lat1, lon2, lat2):
    R = 6371  # Earth radius in km
    dLon = radians(lon2 - lon1)
    dLat = radians(lat2 - lat1)
    a = (sin(dLat/2) ** 2 + cos(radians(lat1)) * 
        cos(radians(lat2)) * sin(dLon/2) ** 2)
    return R * 2 * atan2(sqrt(a), sqrt(1 - a))


if __name__ == "__main__":
    if len(sys.argv) != 5:
        print("Usage: python generate_mock_data.py <num_jobs> <num_vehicles> <max_distance_km> <output_file>")
        sys.exit(1)

    try:
        num_jobs = int(sys.argv[1])
        num_vehicles = int(sys.argv[2])
        max_distance_km = float(sys.argv[3])
        output_file = sys.argv[4]

        mock_data = generate_mock_data(num_jobs, num_vehicles, max_distance_km)
        
        with open(output_file, 'w') as f:
            json.dump(mock_data, f, indent=2)
        
        print(f"Mock data within {max_distance_km}km saved to {output_file}")
    
    except ValueError as e:
        print(f"Error: {str(e)}")
        sys.exit(1)