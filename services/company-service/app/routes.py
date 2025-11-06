from flask import Blueprint, request, jsonify
from app import db
from app.models import Company
from datetime import datetime

bp = Blueprint('companies', __name__, url_prefix='/api/companies')

@bp.route('/', methods=['GET'])
def get_companies():
    """Get all companies with optional filtering"""
    status = request.args.get('status')
    country = request.args.get('country')
    
    query = Company.query
    
    if status:
        query = query.filter_by(status=status)
    if country:
        query = query.filter_by(country=country)
    
    companies = query.all()
    return jsonify([company.to_dict() for company in companies]), 200

@bp.route('/<int:company_id>', methods=['GET'])
def get_company(company_id):
    """Get a specific company by ID"""
    company = Company.query.get_or_404(company_id)
    return jsonify(company.to_dict()), 200

@bp.route('/', methods=['POST'])
def create_company():
    """Create a new company"""
    data = request.get_json()
    
    if not data:
        return jsonify({'error': 'No data provided'}), 400
    
    required_fields = ['name', 'tax_id']
    for field in required_fields:
        if field not in data:
            return jsonify({'error': f'Missing required field: {field}'}), 400
    
    # Check if tax_id already exists
    existing = Company.query.filter_by(tax_id=data['tax_id']).first()
    if existing:
        return jsonify({'error': 'Company with this tax ID already exists'}), 409
    
    company = Company(
        name=data['name'],
        tax_id=data['tax_id'],
        address=data.get('address'),
        city=data.get('city'),
        country=data.get('country'),
        postal_code=data.get('postal_code'),
        phone=data.get('phone'),
        email=data.get('email'),
        website=data.get('website'),
        status=data.get('status', 'active')
    )
    
    db.session.add(company)
    db.session.commit()
    
    return jsonify(company.to_dict()), 201

@bp.route('/<int:company_id>', methods=['PUT'])
def update_company(company_id):
    """Update an existing company"""
    company = Company.query.get_or_404(company_id)
    data = request.get_json()
    
    if not data:
        return jsonify({'error': 'No data provided'}), 400
    
    # Update fields if provided
    if 'name' in data:
        company.name = data['name']
    if 'address' in data:
        company.address = data['address']
    if 'city' in data:
        company.city = data['city']
    if 'country' in data:
        company.country = data['country']
    if 'postal_code' in data:
        company.postal_code = data['postal_code']
    if 'phone' in data:
        company.phone = data['phone']
    if 'email' in data:
        company.email = data['email']
    if 'website' in data:
        company.website = data['website']
    if 'status' in data:
        company.status = data['status']
    
    company.updated_at = datetime.utcnow()
    db.session.commit()
    
    return jsonify(company.to_dict()), 200

@bp.route('/<int:company_id>', methods=['DELETE'])
def delete_company(company_id):
    """Delete a company"""
    company = Company.query.get_or_404(company_id)
    db.session.delete(company)
    db.session.commit()
    
    return jsonify({'message': 'Company deleted successfully'}), 200

@bp.route('/health', methods=['GET'])
def health_check():
    """Health check endpoint"""
    return jsonify({'status': 'healthy', 'service': 'company-service'}), 200
