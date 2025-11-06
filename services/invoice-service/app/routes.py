from flask import Blueprint, request, jsonify
from app import db
from app.models import Invoice
from datetime import datetime

bp = Blueprint('invoices', __name__, url_prefix='/api/invoices')

@bp.route('/', methods=['GET'])
def get_invoices():
    """Get all invoices with optional filtering"""
    company_id = request.args.get('company_id', type=int)
    status = request.args.get('status')
    
    query = Invoice.query
    
    if company_id:
        query = query.filter_by(company_id=company_id)
    if status:
        query = query.filter_by(status=status)
    
    invoices = query.all()
    return jsonify([invoice.to_dict() for invoice in invoices]), 200

@bp.route('/<int:invoice_id>', methods=['GET'])
def get_invoice(invoice_id):
    """Get a specific invoice by ID"""
    invoice = Invoice.query.get_or_404(invoice_id)
    return jsonify(invoice.to_dict()), 200

@bp.route('/', methods=['POST'])
def create_invoice():
    """Create a new invoice"""
    data = request.get_json()
    
    if not data:
        return jsonify({'error': 'No data provided'}), 400
    
    required_fields = ['invoice_number', 'company_id', 'issuer_id', 'seller_id', 'amount']
    for field in required_fields:
        if field not in data:
            return jsonify({'error': f'Missing required field: {field}'}), 400
    
    # Check if invoice_number already exists
    existing = Invoice.query.filter_by(invoice_number=data['invoice_number']).first()
    if existing:
        return jsonify({'error': 'Invoice number already exists'}), 409
    
    invoice = Invoice(
        invoice_number=data['invoice_number'],
        company_id=data['company_id'],
        issuer_id=data['issuer_id'],
        seller_id=data['seller_id'],
        amount=data['amount'],
        currency=data.get('currency', 'USD'),
        status=data.get('status', 'pending'),
        description=data.get('description'),
        due_date=datetime.fromisoformat(data['due_date']) if data.get('due_date') else None
    )
    
    db.session.add(invoice)
    db.session.commit()
    
    return jsonify(invoice.to_dict()), 201

@bp.route('/<int:invoice_id>', methods=['PUT'])
def update_invoice(invoice_id):
    """Update an existing invoice"""
    invoice = Invoice.query.get_or_404(invoice_id)
    data = request.get_json()
    
    if not data:
        return jsonify({'error': 'No data provided'}), 400
    
    # Update fields if provided
    if 'amount' in data:
        invoice.amount = data['amount']
    if 'currency' in data:
        invoice.currency = data['currency']
    if 'status' in data:
        invoice.status = data['status']
    if 'description' in data:
        invoice.description = data['description']
    if 'due_date' in data:
        invoice.due_date = datetime.fromisoformat(data['due_date']) if data['due_date'] else None
    
    invoice.updated_at = datetime.utcnow()
    db.session.commit()
    
    return jsonify(invoice.to_dict()), 200

@bp.route('/<int:invoice_id>', methods=['DELETE'])
def delete_invoice(invoice_id):
    """Delete an invoice"""
    invoice = Invoice.query.get_or_404(invoice_id)
    db.session.delete(invoice)
    db.session.commit()
    
    return jsonify({'message': 'Invoice deleted successfully'}), 200

@bp.route('/health', methods=['GET'])
def health_check():
    """Health check endpoint"""
    return jsonify({'status': 'healthy', 'service': 'invoice-service'}), 200
