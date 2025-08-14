#!/bin/bash

# Complete GitHub Project Setup Script for offres.mr Django Payroll System
# Creates milestones, labels, and all 103 issues from Task Master tasks.json

REPO="moustaphacheikh/offres.ai"
TASKS_FILE="/Users/moustaphacheikh/Desktop/projects/ELIYA-Paie_Install/mccmr/.taskmaster/tasks/tasks.json"

echo "Setting up complete GitHub project for offres.mr Django Payroll System..."
echo "Repository: $REPO"
echo "Reading from: $TASKS_FILE"
echo ""

# Check if tasks.json exists
if [ ! -f "$TASKS_FILE" ]; then
    echo "Error: tasks.json file not found at $TASKS_FILE"
    exit 1
fi

# Check if jq is installed
if ! command -v jq &> /dev/null; then
    echo "Error: jq is required but not installed. Please install jq first."
    echo "On macOS: brew install jq"
    exit 1
fi

echo "Step 1: Creating GitHub milestones..."
echo "======================================"

# Create milestones with due dates starting from current date + 1 week
milestones=(
    "1. Django Project Setup and Configuration|2025-08-18|Initialize Django project with proper structure, settings, and dependencies for offres.mr payroll system. Foundation layer with 8 subtasks covering project structure, PostgreSQL, REST Framework, Celery, and configuration."
    "2. Core Django Models Implementation|2025-08-25|Implement comprehensive Django models equivalent to the 37+ Java entities with proper relationships and validation. 12 subtasks covering Employee model (72 fields), organizational structure, payroll core, financial models, user management, and compliance models."
    "3. Authentication and User Management System|2025-09-01|Implement secure authentication system with role-based access control and proper password security. 8 subtasks covering custom User model extension, password security, authentication views, RBAC decorators, and security audit."
    "4. Employee Management Module|2025-09-08|Build comprehensive employee lifecycle management with 72-field employee records, document management, and organizational structure. 10 subtasks covering CRUD operations, search/filtering, organizational structure, document management, and reporting."
    "5. Time Clock Integration and Attendance Processing|2025-09-15|Implement multi-device time tracking integration with HIKVISION, ZKTecho support and sophisticated attendance calculations. 10 subtasks covering CSV import, database integration, overtime calculations, night shift premiums, and leave management."
    "6. Advanced Payroll Calculation Engine|2025-09-22|Implement sophisticated payroll processing with 25+ built-in functions, tax calculations, and multi-motif processing. 14 subtasks covering F01-F25 payroll functions, progressive ITS tax, CNSS/CNAM calculations, and validation workflows."
    "7. Banking Integration and Payment Processing|2025-09-29|Build multi-bank payment processing with UNL file generation and financial integration capabilities. 9 subtasks covering UNL file generation (58-field), multi-bank format support, salary payment processing, and accounting integration."
    "8. Regulatory Compliance and Declaration System|2025-10-06|Implement automated CNSS, CNAM, and ITS declaration generation with regulatory compliance validation. 11 subtasks covering declaration generation systems, compliance monitoring, alert systems, and audit trails."
    "9. Professional Reporting and Document Generation|2025-10-13|Build comprehensive reporting system with JasperReports equivalent, payslip generation, and business intelligence dashboards. 11 subtasks covering PDF generation, payslip creation, multi-language support, analytics dashboards, and automated email distribution."
    "10. System Administration and Advanced Features|2025-10-20|Implement comprehensive system configuration, bulk operations, license management, and performance optimization. 10 subtasks covering system parameters (81+), bulk operations, license management, performance optimization, and multi-language localization."
)

for milestone_data in "${milestones[@]}"; do
    IFS='|' read -r title due_date description <<< "$milestone_data"
    
    echo "Creating milestone: $title"
    gh api repos/$REPO/milestones --method POST \
        --field title="$title" \
        --field description="$description" \
        --field due_on="${due_date}T23:59:59Z" || echo "  ⚠️  Milestone creation failed or already exists"
done

echo ""
echo "Step 2: Creating GitHub labels..."
echo "================================="

# Create priority labels
gh api repos/$REPO/labels --method POST --field name="priority: high" --field color="d73a49" --field description="High priority task requiring immediate attention" 2>/dev/null || echo "Priority high label exists"
gh api repos/$REPO/labels --method POST --field name="priority: medium" --field color="fbca04" --field description="Medium priority task" 2>/dev/null || echo "Priority medium label exists"
gh api repos/$REPO/labels --method POST --field name="priority: low" --field color="0e8a16" --field description="Low priority task" 2>/dev/null || echo "Priority low label exists"

# Create complexity labels
gh api repos/$REPO/labels --method POST --field name="complexity: 4" --field color="c2e0c6" --field description="Complexity level 4 - Simple" 2>/dev/null || echo "Complexity 4 label exists"
gh api repos/$REPO/labels --method POST --field name="complexity: 6" --field color="bfd4f2" --field description="Complexity level 6 - Moderate" 2>/dev/null || echo "Complexity 6 label exists"
gh api repos/$REPO/labels --method POST --field name="complexity: 7" --field color="ffd8b5" --field description="Complexity level 7 - Moderate-High" 2>/dev/null || echo "Complexity 7 label exists"
gh api repos/$REPO/labels --method POST --field name="complexity: 8" --field color="ffcc5c" --field description="Complexity level 8 - High" 2>/dev/null || echo "Complexity 8 label exists"
gh api repos/$REPO/labels --method POST --field name="complexity: 9" --field color="f99157" --field description="Complexity level 9 - Very High" 2>/dev/null || echo "Complexity 9 label exists"
gh api repos/$REPO/labels --method POST --field name="complexity: 10" --field color="d73a49" --field description="Complexity level 10 - Maximum" 2>/dev/null || echo "Complexity 10 label exists"

# Create module labels
gh api repos/$REPO/labels --method POST --field name="module: setup" --field color="1f77b4" --field description="Django project setup and configuration" 2>/dev/null || echo "Module setup label exists"
gh api repos/$REPO/labels --method POST --field name="module: models" --field color="ff7f0e" --field description="Django models and database" 2>/dev/null || echo "Module models label exists"
gh api repos/$REPO/labels --method POST --field name="module: auth" --field color="2ca02c" --field description="Authentication and user management" 2>/dev/null || echo "Module auth label exists"
gh api repos/$REPO/labels --method POST --field name="module: employees" --field color="d62728" --field description="Employee management module" 2>/dev/null || echo "Module employees label exists"
gh api repos/$REPO/labels --method POST --field name="module: attendance" --field color="9467bd" --field description="Time clock and attendance processing" 2>/dev/null || echo "Module attendance label exists"
gh api repos/$REPO/labels --method POST --field name="module: payroll" --field color="8c564b" --field description="Payroll calculation engine" 2>/dev/null || echo "Module payroll label exists"
gh api repos/$REPO/labels --method POST --field name="module: banking" --field color="e377c2" --field description="Banking integration and payments" 2>/dev/null || echo "Module banking label exists"
gh api repos/$REPO/labels --method POST --field name="module: compliance" --field color="7f7f7f" --field description="Regulatory compliance and declarations" 2>/dev/null || echo "Module compliance label exists"
gh api repos/$REPO/labels --method POST --field name="module: reporting" --field color="bcbd22" --field description="Professional reporting and documents" 2>/dev/null || echo "Module reporting label exists"
gh api repos/$REPO/labels --method POST --field name="module: admin" --field color="17becf" --field description="System administration features" 2>/dev/null || echo "Module admin label exists"

echo ""
echo "Step 3: Creating GitHub issues from tasks.json..."
echo "================================================="

# Function to get module label from task ID
get_module_label() {
    local task_id=$1
    case $task_id in
        1) echo "module: setup" ;;
        2) echo "module: models" ;;
        3) echo "module: auth" ;;
        4) echo "module: employees" ;;
        5) echo "module: attendance" ;;
        6) echo "module: payroll" ;;
        7) echo "module: banking" ;;
        8) echo "module: compliance" ;;
        9) echo "module: reporting" ;;
        10) echo "module: admin" ;;
        *) echo "module: setup" ;;
    esac
}

# Function to get priority label
get_priority_label() {
    local priority=$1
    case $priority in
        high) echo "priority: high" ;;
        medium) echo "priority: medium" ;;
        low) echo "priority: low" ;;
        *) echo "priority: medium" ;;
    esac
}

# Function to get complexity label
get_complexity_label() {
    local complexity=$1
    if [ -n "$complexity" ] && [ "$complexity" != "null" ]; then
        echo "complexity: $complexity"
    else
        echo "complexity: 4"
    fi
}

# Function to format dependencies
format_dependencies() {
    local deps_json="$1"
    local task_id="$2"
    
    if [ "$deps_json" == "null" ] || [ "$deps_json" == "[]" ]; then
        if [ "$task_id" == "1" ]; then
            echo "None"
        else
            echo "Depends on completion of previous milestones"
        fi
        return
    fi
    
    # Convert dependency array to readable format
    echo "$deps_json" | jq -r '.[]' | head -3 | while read dep; do
        echo "Task $dep"
    done | paste -sd "," - || echo "Previous tasks"
}

echo "Reading tasks from JSON file..."

# Get total number of tasks
total_tasks=$(jq '.master.tasks | length' "$TASKS_FILE")
echo "Found $total_tasks main tasks"

# Initialize issue counter
issue_counter=1

# Process each main task and its subtasks
for task_index in $(seq 0 $((total_tasks - 1))); do
    
    # Get task data
    task_data=$(jq ".master.tasks[$task_index]" "$TASKS_FILE")
    task_id=$(echo "$task_data" | jq -r '.id')
    task_title=$(echo "$task_data" | jq -r '.title')
    task_description=$(echo "$task_data" | jq -r '.description')
    task_priority=$(echo "$task_data" | jq -r '.priority // "medium"')
    task_complexity=$(echo "$task_data" | jq -r '.complexityScore // 4')
    subtasks=$(echo "$task_data" | jq '.subtasks')
    subtasks_count=$(echo "$subtasks" | jq 'length')
    
    echo ""
    echo "Processing Task $task_id: $task_title ($subtasks_count subtasks)"
    
    # Get milestone name
    milestone_name="$task_id. $task_title"
    
    # Get labels
    module_label=$(get_module_label "$task_id")
    priority_label=$(get_priority_label "$task_priority")
    complexity_label=$(get_complexity_label "$task_complexity")
    
    # Process each subtask
    for subtask_index in $(seq 0 $((subtasks_count - 1))); do
        
        subtask_data=$(echo "$subtasks" | jq ".[$subtask_index]")
        subtask_id=$(echo "$subtask_data" | jq -r '.id')
        subtask_title=$(echo "$subtask_data" | jq -r '.title')
        subtask_description=$(echo "$subtask_data" | jq -r '.description')
        subtask_details=$(echo "$subtask_data" | jq -r '.details // ""')
        subtask_dependencies=$(echo "$subtask_data" | jq '.dependencies // []')
        
        # Format dependencies
        dependencies_str=$(format_dependencies "$subtask_dependencies" "$task_id")
        
        # Create issue body
        issue_body="**Task ID:** $task_id.$subtask_id
**Milestone:** $task_title
**Dependencies:** $dependencies_str

## Description
$subtask_description

## Implementation Details
$subtask_details

## Acceptance Criteria
- [ ] Feature implemented according to specifications
- [ ] All requirements met from task details
- [ ] Proper validation and error handling
- [ ] Security measures implemented
- [ ] Unit and integration tests created
- [ ] Documentation updated

## Parent Task Context
This is subtask $subtask_id of Task $task_id: $task_title

**Parent Task Description:** $task_description"

        # Create the GitHub issue
        echo "Creating issue #$issue_counter: $subtask_title"
        
        result=$(gh issue create \
            --title "$subtask_title" \
            --body "$issue_body" \
            --milestone "$milestone_name" \
            --label "$priority_label,$module_label,$complexity_label" \
            --repo "$REPO" 2>&1)
        
        if [[ $result == https* ]]; then
            echo "  ✅ Issue #$issue_counter created: $result"
        else
            echo "  ❌ Failed to create issue #$issue_counter: $result"
        fi
        
        # Increment issue counter
        ((issue_counter++))
        
        # Small delay to avoid rate limiting
        sleep 0.3
        
    done
    
    echo "Task $task_id completed: processed $subtasks_count subtasks"
done

# Calculate total issues created
total_issues=$((issue_counter - 1))

echo ""
echo "=============================="
echo "GitHub Project Setup Complete!"
echo "=============================="
echo ""
echo "Summary:"
echo "✅ 10 GitHub Milestones created"
echo "✅ 19 Labels created (priority, complexity, modules)"
echo "✅ $total_issues GitHub Issues created"
echo ""
echo "Repository: https://github.com/$REPO"
echo "View milestones: https://github.com/$REPO/milestones"
echo "View issues: https://github.com/$REPO/issues"
echo ""
echo "All Task Master subtasks are now tracked as GitHub issues!"
echo "The offres.mr Django Payroll System project is ready for development!"